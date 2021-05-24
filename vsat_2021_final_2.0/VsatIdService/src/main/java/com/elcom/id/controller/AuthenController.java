package com.elcom.id.controller;

import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.id.auth.CustomUserDetails;
import com.elcom.id.auth.jwt.JwtTokenProvider;
import com.elcom.id.constant.Constant;
import com.elcom.id.messaging.rabbitmq.RabbitMQClient;
import com.elcom.id.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.id.model.User;
import com.elcom.id.model.dto.AuthorizationResponseDTO;
import com.elcom.id.model.dto.UserFacebookDTO;
import com.elcom.id.model.dto.UserGoogleDTO;
import com.elcom.id.service.AuthService;
import com.elcom.id.service.UserService;
import com.elcom.id.thread.IdThreadManager;
import com.elcom.id.utils.JWTutils;
import com.elcom.id.utils.StringUtil;
import com.elcom.id.validation.UserValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.Response;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Controller
public class AuthenController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenController.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private IdThreadManager idThreadManager;

    //Login
    public ResponseMessage userLogin(Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            String username = (String) bodyParam.get("username");
            String password = (String) bodyParam.get("password");

            String invalidData = new UserValidation().validateLogin(username, password);
            if (invalidData != null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                // Check exist account with email or mobile
                User existUser = userService.findByUserName(username);
                if (existUser == null) {
                    invalidData = "Email hoặc Mobile không tồn tại";
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), invalidData,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), invalidData, null));
                } else {
                    // Xác thực thông tin người dùng Request lên, nếu không xảy ra exception tức là thông tin hợp lệ
                    Authentication authentication = null;
                    try {
                        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                    } catch (AuthenticationException ex) {
                        LOGGER.error(ex.toString());
                        invalidData = "username hoặc mật khẩu không đúng";
                        return new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), invalidData,
                                new MessageContent(HttpStatus.UNAUTHORIZED.value(), invalidData, null));
                    }
                    // Set thông tin authentication vào Security Context
                    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                    if (userDetails.getUser().getStatus() == User.STATUS_LOCK) {
                        response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), Constant.VALIDATION_ACCOUNT_LOCKED, new MessageContent(HttpStatus.UNAUTHORIZED.value(), Constant.VALIDATION_ACCOUNT_LOCKED, null));
                    } else {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        // Trả về jwt cho người dùng.
                        String accessJwt = tokenProvider.generateToken(userDetails);
                        String refreshJwt = JWTutils.createToken(userDetails.getUser().getUuid());
                        //LoginResponse loginResponse = new LoginResponse(accessJwt, refreshJwt);

                        AuthorizationResponseDTO responseDTO = new AuthorizationResponseDTO(userDetails, accessJwt, refreshJwt);

                        response = new ResponseMessage(new MessageContent(responseDTO));
                    }
                }
            }
        }
        return response;
    }

    //Authentication web
    public ResponseMessage authorized(Map<String, String> headerParam) {
        ResponseMessage response = null;
        if (headerParam == null || headerParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
        } else {
            String bearerToken = headerParam.get("authorization");
            // Kiểm tra xem header Authorization có chứa thông tin jwt không
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                try {
                    String jwt = bearerToken.substring(7);
                    LOGGER.info("jwt => " + jwt);
                    String jwtTokenAvailableBlackList = getTokenLogoutInBlackList(jwt);
                    String uuid = tokenProvider.getUuidFromJWT(jwt);
                    UserDetails userDetails = authService.loadUserByUuid(uuid);
                    if (userDetails != null) {
                        User user = ((CustomUserDetails) userDetails).getUser();
                        if (Objects.equals(user.getStatus(), User.STATUS_LOCK)) {
                            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), Constant.VALIDATION_ACCOUNT_LOCKED,
                                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), Constant.VALIDATION_ACCOUNT_LOCKED, null));
                        } else {
                            UsernamePasswordAuthenticationToken authentication
                                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            AuthorizationResponseDTO responseDTO = new AuthorizationResponseDTO((CustomUserDetails) authentication.getPrincipal(), null, null);
                            response = new ResponseMessage(new MessageContent(responseDTO));
                        }
                    } else {
                        response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                                new MessageContent(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null));
                    }

                } catch (Exception ex) {
                    LOGGER.error("failed on set user authentication", ex);
                    response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
                }
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
            }
        }
        return response;
    }

    public ResponseMessage userLogout(Map<String, String> headerParam) {
        ResponseMessage response;
        if (headerParam == null || (!headerParam.containsKey("authorization")
                && !headerParam.containsKey("Authorization"))) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
        } else {

            String bearerToken = headerParam.get("authorization");
            // Kiểm tra xem header Authorization có chứa thông tin jwt không
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                try {
                    String jwt = bearerToken.substring(7);
                    pushTokenLogoutIntoBlackList(jwt);
                    return new ResponseMessage(HttpStatus.OK.value(), "User logged out",
                            new MessageContent(HttpStatus.OK.value(), "User logged out", "User logged out"));
                } catch (Exception ex) {
                    LOGGER.error("failed on set user authentication", ex);
                    response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
                }
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
            }
        }
        return response;
    }
}
