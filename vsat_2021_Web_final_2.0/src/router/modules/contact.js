/** When your routing table is too long, you can split it into small modules**/
import Layout from '@/layout';
const routes = {
  path: '/',
  component: Layout,
  redirect: 'noredirect',
  hidden: false,
  name: 'ContactPage',
  meta: {
    title: 'Danh bạ',
    // icon: 'list',
    imgIcon:require('@/assets/icon/menu/phonebook.png')
  },
  children: [
    {
      path: 'contact',
      component: () => import('@/views/contact/index'),
      meta: {
        title: 'Đối tượng trên biển',
        permissions: ['/contact/vessel']
      },
      children: [
        {
          path: 'vessel',
          component: () => import('@/views/contact/vessel'),
          meta: { title: 'Danh bạ tàu', noCache: true, permissions: ['/contact/vessel'] },
        },
        {
          path: 'object-undefine',
          component: () => import('@/views/contact/objectUndefine'),
          //hidden: true,
          meta: { title: 'Đối tượng không xác định', noCache: true, permissions: ['/contact/object-undefine']},        
        },
        
      ],
    },
    {
      path: 'contact/organization',
      component: () => import('@/views/contact/organization'),
      meta: { 
        title: 'Danh bạ bờ',
        noCache: true,      
        permissions: ['/contact/organization']
       },
    },
    {
      path: 'contact/vessel-detail',
      component: () => import('@/views/contact/vesselDetail'),
      name: 'VesselDetail',
      hidden: true,
      meta: { title: 'Thông tin tàu', noCache: true, permissions: ['/contact/vessel']},        
    },
  ],
};

export default routes;
