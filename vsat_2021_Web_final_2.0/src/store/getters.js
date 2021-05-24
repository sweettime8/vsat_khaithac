const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  userId: state => state.user.id,
  userName: state => state.user.userName,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  user: state => state.user.user,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  permissionRoutes: state => state.permission.routes,
  addRoutes: state => state.permission.addRoutes,
  roleMenuActions : state => state.user.roleMenuActions,
  mobile : state => state.user.mobile,
  birthDay : state => state.user.birthDay,
  email : state => state.user.email,
  gender :  state => state.user.gender,
  address : state => state.user.address,
};
export default getters;
