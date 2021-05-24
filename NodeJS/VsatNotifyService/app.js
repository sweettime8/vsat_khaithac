'use strict';

let express = require('express');
let _ = require('underscore');
let fs = require('fs');
let app = express();
const _cfg = require('./_cfg/_cfg');
const logger = require('./logger/logger');

let server = require('http').createServer(app);
//let server = require('https').createServer({key: fs.readFileSync(_cfg.TLS_PRIVKEY), cert: fs.readFileSync(_cfg.TLS_FULLCHAIN)}, app);

const port = process.argv[2] || _cfg.SERVER_PORT;
server.listen(port, () => {
  logger.instance.info('Server PORT %d', port);
});

const io = require('socket.io').listen(server);

var mapsUuidWithSocketId = {}; //Maps các UUID kèm socketId đã kết nối

process.on('uncaughtException', function (err) {
  logger.instance.error(err);
});

app.get('/', function (req, res) {
  res.status(200).send('Vsat Socket System');
});

function amqpBailErr(err) {
  logger.instance.error(err);
  setTimeout(function() {
    process.exit(1);
  }, 1000);
}

function initAmqpConnection(fn) {
  require('amqplib/callback_api')
  .connect(_cfg.AMQP_CONNECTION_OPTIONS, function(err, conn) {
    if (err)
      amqpBailErr(err);
    fn(conn);
  });
}

initAmqpConnection(function(amqpConn) {
  if( amqpConn ) {
    module.exports = {
      io: io,
      mapsUuidWithSocketId: mapsUuidWithSocketId
    }

	const amqpWorkersQueueModule = require('./amqp/worker');
	
	amqpWorkersQueueModule.doTask(amqpConn, null, null, _cfg.LEARN_SOCKET_WORK_QUEUE_NAME, null);

    io.sockets.use(function(socket, next) {
      logger.instance.info('SOCKET.USE - %s is connected', socket.id);
      return next();
    })
    .on('connection', function (socket) {

      logger.instance.info('SOCKET.ON - %s is connected', socket.id);

      socket.on('disconnect', function () {
      });

      socket.on('error', (error) => {
        logger.instance.error(error);
      });
    });
  }
});
