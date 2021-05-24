/** When your routing table is too long, you can split it into small modules**/
import Layout from '@/layout';

const routes = {
  path: '/account',
  component: Layout,
  redirect: 'noredirect',
  hidden: false,
  name: 'AccountPage',
  meta: {
    title: 'Quản trị',
    // icon: 'admin',
    imgIcon:require('@/assets/icon/menu/settings.png'),
    roles: ['Administrator']
  },
  children: [
    {
      path: '',
      component: () => import('@/views/account/index'),
      meta: {
        title: 'Quản lý tài khoản',
        noCache: true,
        permissions: ['/account']  
      },
    },
    {
      path: 'role',
      component: () => import('@/views/account/role'),
      meta: { 
        title: 'Quản lý nhóm quyền',
        noCache: true ,
        permissions: ['/account/role']  
      },
    },
  ],
};

export default routes;
