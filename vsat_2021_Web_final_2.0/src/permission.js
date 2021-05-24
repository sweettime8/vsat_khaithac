import router from './router';
import store from './store';
import { Message } from 'element-ui';
import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css'; // progress bar style
import { isLogged } from '@/utils/auth';
import getPageTitle from '@/utils/get-page-title';

NProgress.configure({ showSpinner: false }); // NProgress Configuration

const whiteList = ['/login', '/auth-redirect']; // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start();
  // set page title
  document.title = getPageTitle(to.meta.title);

  // determine whether the user has logged in
  const isUserLogged = isLogged();

  if (isUserLogged) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' });
      NProgress.done();
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      const hasRoles = store.getters.roles && store.getters.roles.length > 0;
      if (hasRoles) {
        next();
      } else {
        try {
          // get user info
          // note: roles must be a object array! such as: ['admin'] or ,['manager','editor']
          const info = await store.dispatch('user/getInfo');
          const roles = await store.dispatch('user/getRoleUser');         
          const permissions = await store.dispatch('user/getRolePagePermission');

          // console.log("[permission.js] info : " + info);
          // generate accessible routes map based on roles
          const accessRoutes = await store.dispatch('permission/generateRoutes', { roles, permissions});

          router.addRoutes(accessRoutes);
          next({ ...to, replace: true });
        } catch (error) {
          console.log(error)
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken');
          Message.error(error.message || 'Has Error');
          next(`/login`);
          NProgress.done();
        }
      }
    }

  } else {
    /* has no token*/

    if (whiteList.indexOf(to.matched[0] ? to.matched[0].path : '') !== -1) {
      // in the free login whitelist, go directly
      next();
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login`);
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
