const bcrypt = require('bcryptjs');

exports.login = function (req, res) {
  let filter = {
    'username': req.body.username.toLowerCase()
  };

  let projection = {};

  req.db.users.findOne(filter, projection, function (e, user) {
    if (user) { //  && bcrypt.compareSync(req.body.password, user.password
      delete user.password;
      res.json(user);
    } else {
      //res.status(401).send('Username or password invalid');
      exports.register = function (req, res) {
        if (req.body.username) { //  && req.body.password
          let user = {
            'username': req.body.username,
            'password': "", // bcrypt.hashSync(req.body.password, 8)
            'url': req.body.url || ''
          };
          req.db.users.insert(user, function (e, user) {
            delete user.password;
            res.json(user);
          });
        } else {
          res.status(400).send('Missing required fields');
        }
      };
    }
  });
};
