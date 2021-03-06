package com.elcom.id.controller;

import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.id.auth.CustomUserDetails;
import com.elcom.id.auth.jwt.JwtTokenProvider;
import com.elcom.id.config.ApplicationConfig;
import com.elcom.id.constant.Constant;
import com.elcom.id.mail.MailContentDTO;
import com.elcom.id.messaging.rabbitmq.RabbitMQClient;
import com.elcom.id.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.id.model.User;
import com.elcom.id.model.dto.AuthorizationResponseDTO;
import com.elcom.id.model.dto.OtpExpiredDTO;
import com.elcom.id.model.dto.UserAvatarDTO;
import com.elcom.id.model.dto.UserDetailDTO;
import com.elcom.id.model.dto.UserPagingDTO;
import com.elcom.id.service.AuthService;
import com.elcom.id.service.UserService;
import com.elcom.id.thread.IdThreadManager;
import com.elcom.id.utils.StringUtil;
import com.elcom.id.utils.encrypt.AES;
import com.elcom.id.validation.UserValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

/**
 *
 * @author anhdv
 */
@Controller
public class UserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IdThreadManager idThreadManager;

    public ResponseMessage getAllUser(String urlParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "B???n ch??a ????ng nh???p"));
        } else {

            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            int currentPage = Integer.parseInt(params.get("currentPage"));
            int rowsPerPage = Integer.parseInt(params.get("rowsPerPage"));
            String sort = params.get("sort");
            String keyword = params.get("keyword");
            Integer status = null;
            String strStatus = params.get("status");
            if (StringUtil.isNumeric(strStatus)) {
                status = Integer.parseInt(strStatus);
            }

            if (currentPage == 0 || rowsPerPage == 0) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                Constant.VALIDATION_INVALID_PARAM_VALUE));
            } else {
                if (!StringUtil.isNullOrEmpty(sort) && !"uuid".equalsIgnoreCase(sort)
                        && !"email".equalsIgnoreCase(sort) && !"mobile".equalsIgnoreCase(sort)
                        && !"fullName".equalsIgnoreCase(sort) && !"createdAt".equalsIgnoreCase(sort)) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                    "Kh??ng c?? ki???u sort theo " + sort));
                } else {
                    UserPagingDTO userDTO = userService.findAll(keyword, status, currentPage, rowsPerPage, sort);
                    if (userDTO == null || userDTO.getDataRows() == null || userDTO.getDataRows().isEmpty()) {
                        response = new ResponseMessage(new MessageContent(userDTO.getDataRows(), userDTO.getTotalRows()));
                    } else {
                        response = new ResponseMessage(new MessageContent(userDTO.getDataRows(), userDTO.getTotalRows()));
                    }
                }
            }

        }
        return response;
    }

    public ResponseMessage getDetailUser(String sId, Map<String, String> headerParam) {
        ResponseMessage response;
        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "B???n ch??a ????ng nh???p"));
        } else {
            if (!StringUtil.isNumberic(sId) && !StringUtil.isUUID(sId)) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                User user = userService.findByUuid(sId);
                if (user == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND, new MessageContent(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                } else {
                    UserDetailDTO detailDTO = new UserDetailDTO(user);
                    response = new ResponseMessage(new MessageContent(detailDTO));
                }
            }
        }
        return response;
    }

    public ResponseMessage createUser(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        ResponseMessage response = null;
        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            AuthorizationResponseDTO dto = getAuthorFromToken(headerMap);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "B???n ch??a ????ng nh???p"));
            }
            String userName = (String) bodyParam.get("userName");
            String fullName = (String) bodyParam.get("fullName");
            String password = (String) bodyParam.get("password");
            String mobile = (String) bodyParam.get("mobile");
            String email = (String) bodyParam.get("email");
            String birthDay = (String) bodyParam.get("birthDay");

            String unitName = (String) bodyParam.get("unitName");
            String positionName = (String) bodyParam.get("positionName");
            String departmentName = (String) bodyParam.get("departmentName");
            String description = (String) bodyParam.get("description");

            String address = "";
            String avatar = "";
            String roleCode = (String) bodyParam.get("roleCode");
            int gender = 0;
            int status = 1;
            if (bodyParam.get("gender") != null) {
                gender = (Integer) bodyParam.get("gender");
            }
            if (bodyParam.get("status") != null) {
                status = (Integer) bodyParam.get("status");
            }
            if (bodyParam.get("address") != null) {
                address = (String) bodyParam.get("address");
            }
            if (bodyParam.get("avatar") != null) {
                avatar = (String) bodyParam.get("avatar");
            }

            User user = new User();
            user.setUuid(UUID.randomUUID().toString());
            user.setUserName(userName);
            user.setEmail(email);
            user.setMobile(mobile);
            user.setFullName(fullName);
            user.setPassword(password);
            user.setAddress(address);
            user.setIsDelete(0);
            user.setStatus(status);
            user.setAvatar(avatar);
            user.setGender(gender);
            user.setCreatedBy(dto.getUserName());
            user.setModifyBy(dto.getUserName());

            String invalidData = new UserValidation().validateInsertUser(user);
            if (invalidData != null) {
                response = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                User existUser = null;
                //Check email exist
                existUser = userService.findByEmail(email);
                if (existUser != null) {
                    invalidData = "???? t???n t???i user tr??n h??? th???ng ???ng v???i email " + user.getEmail();
                    response = new ResponseMessage(HttpStatus.OK.value(), invalidData,
                            new MessageContent(HttpStatus.OK.value(), invalidData,
                                    null));
                } else {
                    //Check mobile exist
                    existUser = userService.findByMobile(user.getMobile());
                    if (existUser != null) {
                        invalidData = "???? t???n t???i user tr??n h??? th???ng ???ng v???i mobile " + user.getMobile();
                        response = new ResponseMessage(HttpStatus.OK.value(), invalidData,
                                new MessageContent(HttpStatus.OK.value(), invalidData,
                                        null));
                    } else {
                        //Check user_name exist
                        existUser = userService.findByUserName(user.getUserName());
                        if (existUser != null) {
                            invalidData = "???? t???n t???i user tr??n h??? th???ng ???ng v???i user_name " + user.getUserName();
                            response = new ResponseMessage(HttpStatus.OK.value(), invalidData,
                                    new MessageContent(HttpStatus.OK.value(), invalidData,
                                            null));
                        } else {
                            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                            try {
                                userService.save(user);
                                response = new ResponseMessage(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), new MessageContent(user));
                            } catch (Exception ex) {
                                response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                        new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.toString()));
                            }
                            if (response != null && response.getStatus() == HttpStatus.CREATED.value()) {

                                //Send 2 RBAC => Create ROLE_USER
                                idThreadManager.execute(() -> {
                                    Map<String, Object> rbacRequestBodyParam = new HashMap<>();
                                    rbacRequestBodyParam.put("uuidUser", user.getUuid());
                                    rbacRequestBodyParam.put("roleCode", roleCode);

                                    RequestMessage request = new RequestMessage("POST", RabbitMQProperties.RBAC_RPC_DEFAULT_ROLE_URL,
                                            null, null, rbacRequestBodyParam, null);
                                    LOGGER.info("CREATE USER ROLE (send 2 RBAC service) with param : " + request.toJsonString());
                                    String result = rabbitMQClient.callRpcService(RabbitMQProperties.RBAC_RPC_EXCHANGE,
                                            RabbitMQProperties.RBAC_RPC_QUEUE, RabbitMQProperties.RBAC_RPC_KEY,
                                            request.toJsonString());
                                    if (!StringUtil.isNullOrEmpty(result)) {
                                        try {
                                            ObjectMapper mapper = new ObjectMapper();
                                            ResponseMessage resultResponse = mapper.readValue(result, ResponseMessage.class);
                                            if (resultResponse != null && (resultResponse.getStatus() == HttpStatus.OK.value()
                                                    || resultResponse.getStatus() == HttpStatus.CREATED.value())
                                                    && resultResponse.getData() != null) {
                                                LOGGER.info("CREATE USER ROLE for {} ok ", user.getUuid());
                                            } else {
                                                LOGGER.info("ERROR >>> create USER ROLE for {} ", user.getUuid());
                                            }
                                        } catch (JsonProcessingException ex) {
                                            LOGGER.error("Error to parse json FROM CREATE DEFAULT ROLE (send 2 RBAC service) >>> " + ex.toString());
                                            ex.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage updateUserByUserLogin(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        ResponseMessage response = null;
        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            AuthorizationResponseDTO dto = getAuthorFromToken(headerMap);
            if (dto == null) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "B???n ch??a ????ng nh???p"));
            }
            String uuid = (String) bodyParam.get("uuid");
            String userName = (String) bodyParam.get("userName");
            String fullName = (String) bodyParam.get("fullName");
            String password = (String) bodyParam.get("password");
            String mobile = (String) bodyParam.get("mobile");
            String email = (String) bodyParam.get("email");
            String birthDay = (String) bodyParam.get("birthDay");
            String unitName = (String) bodyParam.get("unitName");
            String positionName = (String) bodyParam.get("positionName");
            String departmentName = (String) bodyParam.get("departmentName");
            String description = (String) bodyParam.get("description");

            String address = "";
            String avatar = "";
            String roleCode = (String) bodyParam.get("roleCode");
            String roleCodeOld = (String) bodyParam.get("roleCodeOld");
            int gender = 0;
            int status = 1;
            if (bodyParam.get("gender") != null) {
                gender = (Integer) bodyParam.get("gender");
            }
            if (bodyParam.get("status") != null) {
                status = (Integer) bodyParam.get("status");
            }
            if (bodyParam.get("address") != null) {
                address = (String) bodyParam.get("address");
            }
            if (bodyParam.get("avatar") != null) {
                avatar = (String) bodyParam.get("avatar");
            }

            User user = new User();
            user.setUuid(uuid);
            user.setUserName(userName);
            user.setEmail(email);
            user.setMobile(mobile);
            user.setFullName(fullName);
            user.setAddress(address);
            user.setIsDelete(0);
            user.setStatus(status);
            user.setAvatar(avatar);
            user.setGender(gender);
            user.setCreatedBy(dto.getUserName());
            user.setModifyBy(dto.getUserName());

            String invalidData = new UserValidation().validateUpdateUserByUserLogin(user);
            if (invalidData != null) {
                response = new ResponseMessage(HttpStatus.OK.value(), invalidData, new MessageContent(HttpStatus.OK.value(), invalidData, null));
            } else {
                User existUser = null;

                existUser = userService.findByUserName(userName);
                if (existUser == null) {
                    invalidData = "Kh??ng t???n t???i user tr??n h??? th???ng ???ng v???i username " + user.getUserName();
                    response = new ResponseMessage(HttpStatus.OK.value(), invalidData,
                            new MessageContent(HttpStatus.OK.value(), invalidData,
                                    null));
                } else {
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    user.setLastUpdate(now);
                    try {
                        userService.update(user);
                        response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(user));
                    } catch (Exception ex) {
                        response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                new MessageContent(HttpStatus.OK.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null));
                    }
                    if (response != null && response.getStatus() == HttpStatus.OK.value()) {

                        //Send 2 RBAC => update ROLE_USER
                        idThreadManager.execute(() -> {
                            Map<String, String> header = new HashMap<>();
                            header.put("authorization", headerMap.get("authorization"));

                            Map<String, Object> rbacRequestBodyParam = new HashMap<>();
                            rbacRequestBodyParam.put("uuidUser", uuid);
                            rbacRequestBodyParam.put("roleCode", roleCodeOld);
                            rbacRequestBodyParam.put("newRole", roleCode);

                            RequestMessage request = new RequestMessage("PUT", "/v1.0/rbac/role/user",
                                    null, null, rbacRequestBodyParam, header);
                            LOGGER.info("Update USER ROLE (send 2 RBAC service) with param : " + request.toJsonString());
                            // TODO khi g???i ??i "request.toJsonString()" th?? l???i unicode c???a ch??? c?? d???u, tr??n windows th?? m???i b???.
                            String result = rabbitMQClient.callRpcService(RabbitMQProperties.RBAC_RPC_EXCHANGE,
                                    RabbitMQProperties.RBAC_RPC_QUEUE, RabbitMQProperties.RBAC_RPC_KEY,
                                    request.toJsonString());
                            if (!StringUtil.isNullOrEmpty(result)) {
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    ResponseMessage resultResponse = mapper.readValue(result, ResponseMessage.class);
                                    if (resultResponse != null && (resultResponse.getStatus() == HttpStatus.OK.value()
                                            || resultResponse.getStatus() == HttpStatus.CREATED.value())
                                            && resultResponse.getData() != null) {
                                        LOGGER.info("UPDATE USER ROLE for {} ok ", user.getUuid());
                                    } else {
                                        LOGGER.info("ERROR >>> UPDATE USER ROLE for {} ", user.getUuid());
                                    }
                                } catch (JsonProcessingException ex) {
                                    LOGGER.error("Error to parse json FROM CREATE DEFAULT ROLE (send 2 RBAC service) >>> " + ex.toString());
                                    ex.printStackTrace();
                                }
                            }
                        });
                    }

                }
            }
        }
        return response;
    }

    public ResponseMessage deleteUser(String sId) {
        ResponseMessage response = null;

        if (!StringUtil.isNumberic(sId) && !StringUtil.isUUID(sId)) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        } else {
            User user = userService.findByUuid(sId);
            if (user == null) {
                response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND, new MessageContent(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
            } else {
                userService.remove(user);
                response = new ResponseMessage(new MessageContent(sId));
            }
        }
        return response;
    }

    public ResponseMessage updateUser(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p",
                    new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p",
                            "B???n ch??a ????ng nh???p"));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                Constant.VALIDATION_INVALID_PARAM_VALUE));
            } else {
                String uuid = dto.getUuid();//(String) bodyParam.get("uuid");
                String mobile = (String) bodyParam.get("mobile");
                String fullName = (String) bodyParam.get("fullName");
                String avatar = (String) bodyParam.get("avatar");
                String address = (String) bodyParam.get("address");
                String birthDay = (String) bodyParam.get("birthDay");
                Integer gender = (Integer) bodyParam.get("gender");

                User user = new User();
                user.setUuid(uuid);
                user.setMobile(mobile);
                user.setFullName(fullName);
                user.setAvatar(avatar);
                user.setAddress(address);
                user.setBirthDay(birthDay);
                user.setGender(gender);

                String invalidData = new UserValidation().validateUpdateUserByUserLogin(user);
                if (invalidData != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, invalidData));
                } else {
                    if (!user.getUuid().equals(dto.getUuid())) {
                        response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "B???n kh??ng ???????c ph??p s???a th??ng tin c???a uuid " + user.getUuid(),
                                new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng ???????c ph??p s???a th??ng tin c???a uuid " + user.getUuid(),
                                        "B???n kh??ng ???????c ph??p s???a th??ng tin c???a uuid " + user.getUuid()));
                    } else {
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        user.setLastUpdate(now);
                        try {
                            userService.update(user);
                            response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(user));
                        } catch (Exception ex) {
                            response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), "L???i kh??ng c???p nh???t " + ex.toString(),
                                    new MessageContent(HttpStatus.NOT_MODIFIED.value(), "L???i kh??ng c???p nh???t " + ex.toString(), ex.toString()));
                        }
                        if (response.getStatus() == HttpStatus.OK.value()) {
                            //Avatar update => Score update avatar
                        }
                    }
                }
            }
        }
        return response;
    }

    private AuthorizationResponseDTO getAuthorFromToken(Map<String, String> headerParam) {
        if (headerParam == null || (!headerParam.containsKey("authorization")
                && !headerParam.containsKey("Authorization"))) {
            return null;
        }
        String bearerToken = headerParam.get("authorization");
        // Ki???m tra xem header Authorization c?? ch???a th??ng tin jwt kh??ng
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            try {
                String jwt = bearerToken.substring(7);
                String uuid = tokenProvider.getUuidFromJWT(jwt);
                UserDetails userDetails = authService.loadUserByUuid(uuid);
                if (userDetails != null) {
                    User user = ((CustomUserDetails) userDetails).getUser();
                    if (user.getStatus() == User.STATUS_LOCK) {
                        return null;
                    } else {
                        UsernamePasswordAuthenticationToken authentication
                                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        AuthorizationResponseDTO responseDTO = new AuthorizationResponseDTO((CustomUserDetails) authentication.getPrincipal(), null, null);
                        return responseDTO;
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("failed on set user authentication", ex);
                return null;
            }
        }
        return null;
    }

    public ResponseMessage findByUuid(Map<String, String> headerParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;

        //AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        //if (dto == null) {
        //    response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
        //            new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
        //                    "B???n ch??a ????ng nh???p"));
        //} else {
        List<String> uuidList = (List<String>) bodyParam.get("uuids");
        if (uuidList == null || uuidList.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            "uuids kh??ng ???????c b??? tr???ng ho???c kh??ng ????ng ?????nh d???ng array"));
        } else {
            try {
                List<User> userList = userService.findByUuidIn(uuidList);
                if (userList == null || userList.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Kh??ng t??m th???y user ???ng v???i list uuid",
                            new MessageContent(HttpStatus.NO_CONTENT.value(), "Kh??ng t??m th???y user ???ng v???i list uuid",
                                    "Kh??ng t??m th???y user ???ng v???i list uuid"));
                } else {
                    response = new ResponseMessage(new MessageContent(userList));
                }
            } catch (Exception ex) {
                response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "L???i kh??ng c???p nh???t " + ex.toString(),
                        new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), "L???i kh??ng c???p nh???t " + ex.toString(), ex.toString()));
                ex.printStackTrace();
            }
        }
        //}
        return response;
    }

    public ResponseMessage updatePassword(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "B???n ch??a ????ng nh???p"));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                Constant.VALIDATION_INVALID_PARAM_VALUE));
            } else {
                String curentPassword = (String) bodyParam.get("curentPassword");
                String newPassword = (String) bodyParam.get("newPassword");
                String rePassword = (String) bodyParam.get("rePassword");

                String invalidData = new UserValidation().validateUpdatePassword(dto, curentPassword, newPassword, rePassword);
                if (invalidData != null) {
                    response = new ResponseMessage(HttpStatus.OK.value(), invalidData,
                            new MessageContent(HttpStatus.OK.value(), invalidData, null));
                } else {
                    User user = userService.findByUuid(dto.getUuid());
                    boolean checkPassword = false;

                    //Check currentPassword
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String dbPassword = user.getPassword();
                    checkPassword = passwordEncoder.matches(curentPassword, dbPassword);

                    if (checkPassword) {
                        // Encode new password and store it
                        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

                        try {
                            if (userService.changePassword(user)) {
                                // X??c th???c th??ng tin ng?????i d??ng Request l??n, n???u kh??ng x???y ra exception t???c l?? th??ng tin h???p l???
                                Authentication authentication = null;
                                try {
                                    authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), newPassword));
                                } catch (AuthenticationException ex) {
                                    LOGGER.error("Error to set new authentication >>> " + ex.toString());
                                }
                                // Set th??ng tin authentication m???i v??o Security Context
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(bodyParam));
                            } else {
                                response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(),
                                        new MessageContent(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(), null));
                            }
                        } catch (Exception ex) {
                            response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                    new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.toString()));
                        }
                    } else {
                        // Report error 
                        response = new ResponseMessage(HttpStatus.OK.value(), "M???t kh???u hi???n t???i kh??ng ????ng",
                                new MessageContent(HttpStatus.OK.value(), "M???t kh???u hi???n t???i kh??ng ????ng",
                                        null));
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage resetPassword(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                    new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "B???n ch??a ????ng nh???p"));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                Constant.VALIDATION_INVALID_PARAM_VALUE));
            } else {
                String uuid = (String) bodyParam.get("uuid");
                String newPassword = (String) bodyParam.get("newPassword");
                String rePassword = (String) bodyParam.get("rePassword");

                String invalidData = new UserValidation().validateResetPassword(dto, newPassword, rePassword);
                if (invalidData != null) {
                    response = new ResponseMessage(HttpStatus.OK.value(), invalidData,
                            new MessageContent(HttpStatus.OK.value(), invalidData, null));
                } else {
                    User user = userService.findByUuid(uuid);
                    boolean checkPassword = false;

                    // Encode new password and store it
                    user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

                    try {
                        if (userService.changePassword(user)) {
                            // X??c th???c th??ng tin ng?????i d??ng Request l??n, n???u kh??ng x???y ra exception t???c l?? th??ng tin h???p l???
                            Authentication authentication = null;
                            try {
                                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), newPassword));
                            } catch (AuthenticationException ex) {
                                LOGGER.error("Error to set new authentication >>> " + ex.toString());
                            }
                            // Set th??ng tin authentication m???i v??o Security Context
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(bodyParam));
                        } else {
                            response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(),
                                    new MessageContent(HttpStatus.OK.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(), null));
                        }
                    } catch (Exception ex) {
                        response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.toString()));
                    }

                }
            }
        }
        return response;
    }

    public ResponseMessage checkUserExist(String urlParam) {
        ResponseMessage response = null;
        if (StringUtil.isNullOrEmpty(urlParam)) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            null));
        } else {
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String type = params.get("type");
            String email = params.get("email");
            String mobile = params.get("mobile");

            if ("email".equalsIgnoreCase(type)) {
                if (!StringUtil.validateEmail(email)) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                    "email kh??ng ???????c tr???ng ho???c kh??ng ????ng ?????nh d???ng"));
                } else {
                    User user = userService.findByEmail(email);
                    if (user == null) {
                        response = new ResponseMessage(new MessageContent("Kh??ng t??m th???y user ???ng v???i email " + email));
                    } else {
                        response = new ResponseMessage(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
                                new MessageContent(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
                                        "Email ???? ???????c ????ng k?? b???i t??i kho???n kh??c"));
                    }
                }
            } else if ("mobile".equalsIgnoreCase(type)) {
                if (!StringUtil.checkMobilePhoneNumberNew(mobile)) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                    "mobile kh??ng ???????c tr???ng ho???c kh??ng ????ng ?????nh d???ng"));
                } else {
                    User user = userService.findByMobile(mobile);
                    if (user == null) {
                        response = new ResponseMessage(new MessageContent("Kh??ng t??m th???y user ???ng v???i mobile " + mobile));
                    } else {
                        response = new ResponseMessage(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
                                new MessageContent(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
                                        "S??? ??i???n tho???i n??y ???? ???????c x??c th???c b???i m???t t??i kho???n kh??c. M???i s??? ??i???n tho???i ch??? c?? th??? x??c th???c cho m???t t??i kho???n"));
                    }
                }
            } else {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                "type ch??? nh???n gi?? tr??? b???ng email ho???c mobile"));
            }
        }
        return response;
    }

    public ResponseMessage forgotPassword(String urlParam) {
        ResponseMessage response = null;
        if (StringUtil.isNullOrEmpty(urlParam)) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            Constant.VALIDATION_INVALID_PARAM_VALUE));
        } else {
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String email = params.get("email");

            if (!StringUtil.validateEmail(email)) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "email kh??ng ???????c tr???ng ho???c kh??ng ????ng ?????nh d???ng",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "email kh??ng ???????c tr???ng ho???c kh??ng ????ng ?????nh d???ng",
                                null));
            } else {
                User user = userService.findByEmail(email);
                if (user == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y user ???ng v???i email " + email,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y user ???ng v???i email " + email,
                                    null));
                } else {
                    //Code m???i tr??? v??? link
                    String genCheckInfo = user.getUuid() + "&" + (System.currentTimeMillis()
                            + ApplicationConfig.FORGOTPASS_EXPIRED_TIME * 60 * 1000);
                    String token = AES.encryptAESbase(genCheckInfo, Constant.AES_KEY);
                    String link = ApplicationConfig.FRONTEND_FORGOTPASS_URL + "?token=" + token;
                    MailContentDTO item = new MailContentDTO();
                    item.setType("one");
                    item.setFromName("Vitalsign.com.vn");
                    item.setEmailTitle(String.format(Constant.MAIL_FORGOT_PW_TITLE, user.getEmail()));
                    item.setEmailContent(String.format(Constant.MAIL_FORGOT_PW_CONTENT_LINK,
                            user.getFullName(), link, link, ApplicationConfig.FORGOTPASS_EXPIRED_TIME));
                    item.setEmailTo(email);
                    if (idThreadManager.sendEmail(item)) {
                        response = new ResponseMessage(new MessageContent("G???i email qu??n m???t kh???u th??nh c??ng"));
                    } else {
                        response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                        null));
                    }

                }
            }
        }
        return response;
    }

    public ResponseMessage findAvatarByUuid(Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        List<String> uuidList = (List<String>) bodyParam.get("uuids");
        if (uuidList == null || uuidList.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            "uuids kh??ng ???????c b??? tr???ng ho???c kh??ng ????ng ?????nh d???ng array"));
        } else {
            try {
                List<User> userList = userService.findByUuidIn(uuidList);
                if (userList == null || userList.isEmpty()) {
                    LOGGER.info("userList nulllllllll");
                    response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Kh??ng t??m th???y user avatar ???ng v???i list uuid",
                            new MessageContent(HttpStatus.NO_CONTENT.value(), "Kh??ng t??m th???y user avatar ???ng v???i list uuid",
                                    "Kh??ng t??m th???y user avatar ???ng v???i list uuid"));
                } else {
                    LOGGER.info("userList size: " + userList.size());
                    List<UserAvatarDTO> userAvatarList = UserAvatarDTO.getAvatarList(userList);
                    response = new ResponseMessage(new MessageContent(userAvatarList));
                }
            } catch (Exception ex) {
                response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "L???i kh??ng c???p nh???t " + ex.toString(),
                        new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), "L???i kh??ng c???p nh???t " + ex.toString(), ex.toString()));
                ex.printStackTrace();
            }
        }
        return response;
    }

    public ResponseMessage updateUserInternal(Map<String, Object> bodyParam, Map<String, String> headerParam, String pathParam) {
        ResponseMessage response = null;

        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            null));
        } else {
            String uuid = pathParam;
            String mobile = (String) bodyParam.get("mobile");
            String fullName = (String) bodyParam.get("fullName");
            String skype = (String) bodyParam.get("skype");
            String facebook = (String) bodyParam.get("facebook");
            String avatar = (String) bodyParam.get("avatar");
            String address = (String) bodyParam.get("address");
            String birthDay = (String) bodyParam.get("birthDay");
            Integer gender = (Integer) bodyParam.get("gender");

            User user = new User();
            user.setUuid(uuid);
            user.setMobile(mobile);
            user.setFullName(fullName);
            user.setAvatar(avatar);
            user.setAddress(address);
            user.setBirthDay(birthDay);
            user.setGender(gender);

            String invalidData = new UserValidation().validateUpdateUser(user);
            if (invalidData != null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                user.setLastUpdate(now);
                try {
                    userService.update(user);
                    response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(user));
                } catch (Exception ex) {
                    response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), "L???i kh??ng c???p nh???t " + ex.toString(),
                            new MessageContent(HttpStatus.NOT_MODIFIED.value(), "L???i kh??ng c???p nh???t " + ex.toString(), null));
                }
            }
        }
        return response;
    }

    public ResponseMessage checkToken(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            null));
        } else {
            String token = (String) bodyParam.get("token");
            if (StringUtil.isNullOrEmpty(token)) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                null));
            } else {
                String decryptResult = AES.decryptAESbase(token, Constant.AES_KEY);
                if (StringUtil.isNullOrEmpty(decryptResult)) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Token kh??ng ????ng",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Token kh??ng ????ng",
                                    null));
                } else {
                    String[] decryptInfo = decryptResult.split("&");
                    if (decryptInfo == null || decryptInfo.length < 2) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Token kh??ng ????ng",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Token kh??ng ????ng",
                                        null));
                    } else {
                        String userUuid = decryptInfo[0];
                        User user = userService.findByUuid(userUuid);

                        if (user == null || (user.getIsDelete() != null && (user.getStatus() == -1 || user.getIsDelete() == 1))) {
                            response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                    new MessageContent(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                        } else {
                            long now = System.currentTimeMillis();
                            long expiredTime = Long.parseLong(decryptInfo[1]);
                            if (expiredTime < now) {
                                response = new ResponseMessage(HttpStatus.REQUEST_TIMEOUT.value(), "Link qu??n m???t kh???u h???t h???n",
                                        new MessageContent(HttpStatus.REQUEST_TIMEOUT.value(), "Link qu??n m???t kh???u h???t h???n", null));
                            } else {
                                response = new ResponseMessage(new MessageContent(bodyParam));
                            }
                        }
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage updateForgotPassword(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            null));
        } else {
            String token = (String) bodyParam.get("token");
            String newPassword = (String) bodyParam.get("newPassword");
            String rePassword = (String) bodyParam.get("rePassword");

            String invalidData = new UserValidation().validateUpdateForgotPassword(token, newPassword, rePassword);
            if (invalidData != null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                String decryptResult = AES.decryptAESbase(token, Constant.AES_KEY);
                if (StringUtil.isNullOrEmpty(decryptResult)) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Token kh??ng ????ng",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Token kh??ng ????ng", null));
                } else {
                    String[] decryptInfo = decryptResult.split("&");
                    if (decryptInfo == null || decryptInfo.length < 2) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Token kh??ng ????ng",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Token kh??ng ????ng", null));
                    } else {
                        String userUuid = decryptInfo[0];
                        User user = userService.findByUuid(userUuid);

                        if (user == null || (user.getIsDelete() != null && (user.getStatus() == -1 || user.getIsDelete() == 1))) {
                            response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                    new MessageContent(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                        } else {
                            long now = System.currentTimeMillis();
                            //15ph + 1ph cho ngoi nhin la 16ph
                            long expiredTime = Long.parseLong(decryptInfo[1]) + 60 * 1000;
                            if (expiredTime < now) {
                                response = new ResponseMessage(HttpStatus.REQUEST_TIMEOUT.value(), "Link qu??n m???t kh???u h???t h???n",
                                        new MessageContent(HttpStatus.REQUEST_TIMEOUT.value(), "Link qu??n m???t kh???u h???t h???n", null));
                            } else {
                                //Update password
                                user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

                                try {
                                    if (userService.changePassword(user)) {
                                        // X??c th???c th??ng tin ng?????i d??ng Request l??n, n???u kh??ng x???y ra exception t???c l?? th??ng tin h???p l???
                                        Authentication authentication = null;
                                        try {
                                            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), newPassword));
                                        } catch (AuthenticationException ex) {
                                            LOGGER.error("Error to set new authentication >>> " + ex.toString());
                                        }
                                        // Set th??ng tin authentication m???i v??o Security Context
                                        SecurityContextHolder.getContext().setAuthentication(authentication);
                                        response = new ResponseMessage(new MessageContent(bodyParam));
                                    } else {
                                        response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                                new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null));
                                    }
                                } catch (Exception ex) {
                                    response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                            new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.toString(), null));
                                }
                            }
                        }
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage sendEmail(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;
        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            null));
        } else {
            String emailTo = (String) bodyParam.get("emailTo");
            String title = (String) bodyParam.get("title");
            String content = (String) bodyParam.get("content");
            String sign = (String) bodyParam.get("sign");
            LOGGER.info("emailTo: {}, title: {}, content: {}, sign: {}", emailTo, title, content, sign);
            String invalidData = new UserValidation().validateSendEmail(emailTo, title, content, sign);
            if (invalidData != null) {
                LOGGER.info("invalidData: {}", invalidData);
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                MailContentDTO item = new MailContentDTO();
                item.setType("one");
                item.setFromName("VitalSign.com.vn");
                item.setEmailTitle(title);
                item.setEmailContent(content);
                item.setEmailTo(emailTo);
                if (idThreadManager.sendEmail(item)) {
                    LOGGER.info("Send email ok");
                    response = new ResponseMessage(new MessageContent("G???i email th??nh c??ng"));
                } else {
                    LOGGER.info("Send email INTERNAL_SERVER_ERROR");
                    response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                    null));
                }
            }
        }
        return response;
    }

    public ResponseMessage updateBu(Map<String, Object> bodyParam, Map<String, String> headerParam) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = getAuthorFromToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p",
                    new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p",
                            "B???n ch??a ????ng nh???p"));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                Constant.VALIDATION_INVALID_PARAM_VALUE));
            } else {
                String uuid = (String) bodyParam.get("uuid");
                String mobile = (String) bodyParam.get("phoneNumber");
                String fullName = (String) bodyParam.get("businessName");
                String address = (String) bodyParam.get("address");
                String avatar = (String) bodyParam.get("avatar");
                String email = (String) bodyParam.get("email");
                Integer isDelete = (Integer) bodyParam.get("isDeleted");

                User user = new User();
                user.setUuid(uuid);
                user.setMobile(mobile);
                user.setFullName(fullName);
                user.setAddress(address);
                user.setAvatar(avatar);
                user.setEmail(email);

                String invalidData = new UserValidation().validateUpdateUser(user);
                if (invalidData != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, invalidData));
                } else {
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    user.setLastUpdate(now);
                    try {
                        userService.update(user);
                        response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(user));
                    } catch (Exception ex) {
                        response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), "L???i kh??ng c???p nh???t " + ex.toString(),
                                new MessageContent(HttpStatus.NOT_MODIFIED.value(), "L???i kh??ng c???p nh???t " + ex.toString(), ex.toString()));
                    }
                    if (response.getStatus() == HttpStatus.OK.value()) {
                        //Avatar update => Score update avatar
                    }
                }
            }
        }
        return response;
    }
}
