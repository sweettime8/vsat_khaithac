'use strict';

let express = require('express');
let router = express.Router();

router.get('/', function (req, res) {
  console.log('go');
  res.send('Vsat Socket');
});

module.exports = router;
