/** When your routing table is too long, you can split it into small modules**/
import Layout from '@/layout';

const routes = {
  path: '/statistic',
  component: Layout,
  redirect: 'noredirect',
  hidden: false,
  name: 'StatisticPage',
  meta: {
    title: 'Thống kê',
    // icon: 'chart',
    imgIcon:require('@/assets/icon/menu/increase.png')
  },
  children: [
    {
      path: '/statistic/flrl',
      component: () => import('@/views/statistic/flrl'),
      meta: {
        title: 'Thống kê dung lượng theo chiều dữ liệu',
        noCache: true,
        permissions: ['/statistic/flrl']  
      },
    },
    {
        path: '/statistic/amount',
        component: () => import('@/views/statistic/amount'),
        meta: {
          title: 'Thống kê số bản tin vị trí theo chiều',
          noCache: true,
          permissions: ['/statistic/amount']  
        },
    },
    {
        path: '/statistic/media',
        component: () => import('@/views/statistic/media'),
        meta: {
          title: 'Thống kê dung lượng media theo chiều',
          noCache: true,
          permissions: ['/statistic/media']  
        },
    },
    {
        path: '/statistic/total',
        component: () => import('@/views/statistic/total'),
        meta: {
          title: 'Thống kê dung lượng theo nguồn',
          noCache: true,
          permissions: ['/statistic/total']  
        },
    },  
    {
        path: '/statistic/typeservice',
        component: () => import('@/views/statistic/typeservice'),
        meta: {
          title: 'Thống kê theo loại hình dịch vụ',
          noCache: true,
          permissions: ['/statistic/typeservice']  
        },
    }, 
    {
        path: '/statistic/pcap',
        component: () => import('@/views/statistic/pcap'),
        meta: {
          title: 'Thống kê theo loại giao thức',
          noCache: true,
          permissions: ['/statistic/pcap']  
        },
    }, 
    {
        path: '/statistic/object',
        component: () => import('@/views/statistic/object'),
        meta: {
          title: 'Thống kê theo đối tượng',
          noCache: true,
          permissions: ['/statistic/object']  
        },
    },                  
  ],
};

export default routes;
