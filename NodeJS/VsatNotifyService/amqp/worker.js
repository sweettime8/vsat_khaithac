'use strict';

const logger = require('../logger/logger');
const _cfg = require('../_cfg/_cfg');

let app = require('../app');

exports.doTask = function (amqpConn, exchangeName, exchangeType, queueName, routingKey) {
  amqpConn.createChannel(function(err, channel) {
      if (err) {
        logger.instance.error('worker.doTask.amqpConn.createChannel.ex: %s', err);
        throw err;
      }

      channel.assertQueue(queueName, {
          durable: true
      });
      channel.prefetch(1);
      logger.instance.info("worker.waiting for msg in %s.........", queueName);
      channel.consume(queueName, function(msg) {
        if(msg) {
            try {
                logger.instance.info("worker.received %s", msg.content.toString());
                
                let dataReceive = JSON.parse(msg.content.toString());
                if( dataReceive && dataReceive.requestPath ) {
                    
                    switch (dataReceive.requestPath) {
                    
                        case _cfg.SRV_RULE_NOTIFY_INSIDE_AREA_REQ_PATH:
                            sendMessageToUser(dataReceive.bodyParam, function(fn) {
                                if(fn)
                                    logger.instance.info("worker.sendMessageToUser status: %s", JSON.stringify(fn));
                            });                              
                            break;

                        default:
                            break;
                    }
                }
            }catch(ex) {
                logger.instance.error("worker.sent msg to queueName [%s] error, ex: [%s]", queueName, ex);
            }finally {
                channel.ack(msg);
            }
        }
      }, { noAck: false });
  });
};

function sendMessageToUser(msg, fn) {
    if( !msg || !msg.messageContent ) {
        logger.instance.error('worker.sendMessageToUser.data is invalid');
        return;
    }
    let emitResult = app.io.emit('NOTIFY_MESSAGE', msg.messageContent.replace('đ', 'Đ').replace('�?', 'ỏ'));
    if( emitResult )
        fn({ status: 'SEND_OK!', code: 200 });
    else
        fn({ status: 'SEND_FAILED!', code: 500 });
}
