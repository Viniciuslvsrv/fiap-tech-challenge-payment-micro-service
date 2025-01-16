// Conecta ao MongoDB usando o usuário root
db = connect("mongodb://admin:password@localhost:27017/admin");

// Muda para o banco "lanchonete"
db = db.getSiblingDB("lanchonete");

// Cria uma coleção chamada "payments"
db.createCollection("payments");

// Cria um usuário com permissões de root
db.createUser({
  user: "admin",
  pwd: "password",
  roles: [ { role: "root", db: "admin" } ]
});