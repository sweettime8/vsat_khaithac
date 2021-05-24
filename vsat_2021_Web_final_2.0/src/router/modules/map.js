/** When your routing table is too long, you can split it into small modules**/
import Layout from '@/layout';

const routes = {
  path: '/map',
  component: Layout,
  redirect: 'noredirect',
  hidden: false,
  name: 'MapPage',
  meta: {
    title: 'Khai thác',
    //icon: 'el-icon-map-location',
    imgIcon:require('@/assets/icon/menu/ship.png')
    // roles: ['ADMIN']
  },
  children: [
    {
      path: '/map/overall',
      component: () => import('@/views/map/overall'),
      name: 'ais',
      meta: { title: 'Khai thác tổng thể', noCache: true },
    },
    {
        path: '/map/voyage',
        name: 'voyage',
        component: () => import('@/views/map/voyage'),
        meta: { title: 'Khai thác tập trung', noCache: true },
      }
      ,{
        path: '/map/area',
        name: 'area',
        component: () => import('@/views/map/area'),
        meta: { title: 'Vùng', noCache: true },
      }
  ],
};

export default routes;
