'use strict';

let TECH_ENV = '_anhdv';

module.exports = {

    'SERVER_PORT': 5001,
    'TLS_PRIVKEY': 'ssl-key/privkey.pem',
    'TLS_FULLCHAIN': 'ssl-key/fullchain.pem',

    'AMQP_CONNECTION_OPTIONS' : {
        protocol: 'amqp',
        hostname: '103.21.151.183',
        port:      5672,
        username: 'test',
        password: 'test',
        locale:   'vi_VN'
    },

    /** Authenticate service */
    'AMQP_AUTHENTICATE_URL': '/v1.0/user/authentication',
    'USER_RPC_EXCHANGE_NAME': 'learn_user_rpc_exchange',
    'USER_RPC_EXCHANGE_TYPE': 'direct',
    'USER_RPC_QUEUE_NAME':    'learn_user_rpc_queue',
    'USER_RPC_ROUTING_KEY':   'learn_user_rpc',

    /** Rule worker queue requestPath */
    'SRV_RULE_NOTIFY_INSIDE_AREA_REQ_PATH': '/vsat-rule/action/notify-area',

    /** Queue for Socket */
    'LEARN_SOCKET_WORK_QUEUE_NAME':    'vsat_rule_worker_queue' + TECH_ENV,
};