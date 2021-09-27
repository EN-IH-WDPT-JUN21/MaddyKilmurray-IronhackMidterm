



GET - /users - ADMIN
GET - /users/{username} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
GET - /users/byid/{id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
POST - /users/new/admin - ADMIN
POST - /users/new/accountholder - ADMIN
POST - /users/new/thirdparty - ADMIN
PATCH - /users/update/logindetails/{id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
PATCH - /users/update/username/{id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
PATCH - /users/update/password/{id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
PATCH - /users/update/accountholder/{id} - ADMIN, ACCOUNTHOLDER (with username)
PATCH - /users/update/thirdparty/{id} - ADMIN, THIRDPARTY (with secret key)
PATCH - /users/update/admin/{id} - ADMIN
DELETE - /users/remove/{id} - ADMIN

GET - /accounts - ADMIN
GET - /accounts/byid/{id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
POST - /accounts/new/checking - ADMIN
POST - /accounts/new/savings - ADMIN
POST - /accounts/new/creditcard - ADMIN
POST - /accounts/new/thirdparty - ADMIN

/accounts/getbalance/checking/{account_id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
/accounts/getbalance/studentchecking/{account_id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
/accounts/getbalance/savings/{account_id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
/accounts/getbalance/creditcard/{account_id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
/accounts/getbalance/thirdparty/{account_id} - ADMIN, ACCOUNTHOLDER (with username, their account only), THIRDPARTY (with secret key, their account only)
/accounts/admin/transferfunds/ - ADMIN
/accounts/accountholder/transferfunds/{username} - ADMIN, ACCOUNTHOLDER (with username, their account only)
/accounts/thirdparty/transferfunds/ - ADMIN, THIRDPARTY (with secret key, their account only)