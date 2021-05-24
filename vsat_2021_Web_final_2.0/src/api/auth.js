import request from '@/utils/request';

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data: data,
  });
}

export function getInfo(token="") {
  if (!token){
    return request({
      url: '/user/authentication',
      method: 'post'
    });
  } else {
    return request({
      url: '/user/authentication',
      method: 'post',
      headers: {
        Authorization: 'Bearer ' + token
      }
    });
  }
}

export function getRoleUser(id) {
  return request({
    url: '/rbac/role/user/' + id,
    method: 'get'
  })

}

export function getRolePagePermission(roleCode) {
  return request({
    url: 'rbac/role/page-permission-of-role?roleCode=' + roleCode,
    method: 'get'
  })

}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post',
  });
}
