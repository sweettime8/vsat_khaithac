/** When your routing table is too long, you can split it into small modules**/
import Layout from '@/layout';

const routes = {
  path: '/rule',
  component: Layout,
  redirect: 'noredirect',
  hidden: false,
  name: 'RuleEventPage',
  meta: {
    title: 'QUẢN LÝ LUẬT',
    icon: 'people',
  },
  children: [
    {
      path: '',
      component: () => import('@/views/rule'),
      meta: { title: 'Danh sách luật', noCache: true , permissions: ['/rule']},
    },
    {
      path: '',
      component: () => import('@/views/rule/event'),
      meta: { title: 'Danh sách sự kiện', noCache: true , permissions: ['/rule']},
    }
  ],
};

export default routes;
