/** When your routing table is too long, you can split it into small modules**/
import Layout from '@/layout';

const routes = {
  path: '/media',
  component: Layout,
  redirect: 'noredirect',
  hidden: false,
  name: 'MediaPage',
  meta: {
    title: 'QUẢN LÝ MEDIA',
    // icon: 'people',
    imgIcon:require('@/assets/icon/menu/windows-media-player.png')
  },
  children: [
    {
      path: '',
      component: () => import('@/views/media'),
      meta: { title: 'Danh sách media', noCache: true , permissions: ['/media']},
    },
    {
      path: '/media-processed',
      component: () => import('@/views/media/processed'),
      meta: { title: 'Danh sách media đã xử lý', noCache: true , permissions: ['/media-processed']},
    }
  ],
};

export default routes;
