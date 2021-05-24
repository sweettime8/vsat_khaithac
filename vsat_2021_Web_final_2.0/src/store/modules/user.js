import { login, getInfo, getRoleUser, getRolePagePermission } from '@/api/auth';
import { isLogged, setLogged, removeToken } from '@/utils/auth';
import router, { resetRouter } from '@/router';

const state = {
  id: null,
  user: null,
  token: isLogged(),
  name: '',
  userName:'',
  avatar: '',
  roles: [],
  permissions: [],
  resource: [],
  roleMenuActions: [],
  
};

const mutations = {
  SET_ID: (state, id) => {
    state.id = id;
  },
  SET_TOKEN: (state, token) => {
    state.token = token;
  },
  SET_NAME: (state, name) => {
    state.name = name;
  },
  SET_USERNAME: (state, userName) => {
    state.userName = userName;
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar;
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles;
  },
  SET_PERMISSIONS: (state, permissions) => {
    state.permissions = permissions;
  },
  SET_RESOURCE: (state, resource) => {
    state.resource = resource;
  },
  SET_ROLE_MENU_ACTIONS: (state, roleMenuActions) => {
    state.roleMenuActions = roleMenuActions;
  },
  SET_ADDRESS: (state, address) => {
    state.address = address;
  },
  SET_BIRTHDAY: (state, birthDay) => {
    state.birthDay = birthDay;
  },
  SET_GENDER: (state, gender) => {
    state.gender = gender;
  },
  SET_MOBILE: (state, mobile) => {
    state.mobile = mobile;
  },
  SET_EMAIL: (state, email) => {
    state.email = email;
  },

};

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo;
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password })
        .then(response => {
          const { data } = response;
          commit('SET_TOKEN', data.accessToken);
          setLogged(data.accessToken);
          resolve();
        })
        .catch(error => {
          reject(error);
        });
    });
  },
  //get user info
  getInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getInfo()
        .then(response => {
          const { data } = response;

          if (!data) {
            reject('Verification failed, please Login again.');
          }

          const {userName, fullName, avatar, uuid , address, birthDay, gender, mobile, email } = data;
          
          commit('SET_NAME', fullName);
          commit('SET_USERNAME', userName);
          commit('SET_AVATAR', avatar);
          commit('SET_ID', uuid);
          commit('SET_ADDRESS', address);
          commit('SET_BIRTHDAY', birthDay);
          commit('SET_GENDER', gender);
          commit('SET_MOBILE', mobile);
          commit('SET_EMAIL', email);
          resolve(data);
        })
        .catch(error => {
          reject(error);
        });
    });
  },
  //get role user
  getRoleUser({ commit }) {
    return new Promise((resolve, reject) => {
      getRoleUser(state.id).then(response => {
        const { data } = response;
        let roles = [];

        for (let x = 0; x < data.length; x++) {
          roles.push(data[x].roleCode.roleCode);
        }
        //roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('getInfo: roles must be a non-null array!');
        }
        commit('SET_ROLES', roles);
        resolve(roles);
      })
    })
  },

  getRolePagePermission({ commit }){
    return new Promise((resolve, reject) => {
      getRolePagePermission(state.roles[0]).then(response => {
        const { data } = response;
        let permissions = [];        
        let roleMenuActions = [];

        for (let x = 0; x < data.length; x++) {
          let roleMenuAction = {
            'pageUrl': data[x].pageUrl,
            'canCreate': data[x].canCreate,
            'canRead': data[x].canRead,
            'canUpdate': data[x].canUpdate,
            'canDelete': data[x].canDelete,
          };

          permissions.push(data[x].pageUrl);
          roleMenuActions.push(roleMenuAction);
        }
        // //permissions must be a non-empty array
        if (!permissions || permissions.length <= 0) {
          reject('getRolePagePermission: permissions must be a non-null array!');
        }
        commit('SET_ROLE_MENU_ACTIONS', roleMenuActions);

        resolve(permissions);
      })
      
    })
  },

  // user logout
  logout({ commit }) {
    return new Promise((resolve) => {
      commit('SET_TOKEN', '');
      commit('SET_ROLES', []);
      removeToken();
      resetRouter();
      resolve();
    });
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '');
      commit('SET_ROLES', []);
      removeToken();
      resolve();
    });
  },

  // Dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(resolve => {
      // const token = role + '-token';

      // commit('SET_TOKEN', token);
      // setLogged(token);

      // const { roles } = await dispatch('getInfo');

      const roles = [role.name];
      const permissions = role.permissions.map(permission => permission.name);
      commit('SET_ROLES', roles);
      commit('SET_PERMISSIONS', permissions);
      resetRouter();

      // generate accessible routes map based on roles
      const accessRoutes = dispatch('permission/generateRoutes', { roles, permissions });

      // dynamically add accessible routes
      router.addRoutes(accessRoutes);

      resolve();
    });
  },

  // Update avatar
  updateAvatar({ commit }, avatar) {
    return new Promise((resolve) => {
      commit('SET_AVATAR', avatar);
      resolve();
    });
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
