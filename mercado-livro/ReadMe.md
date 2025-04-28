# MERCADO LIVRO
- Com base no curso `Kotlin e Spring do ZERO ao Avançado -  Gustavo Pinoti, Daniel Poletto Donato`

## mysql, está num docker-compose
- Iniciar o docker-compose
`docker-compose up -d`

## Swagger
- http://localhost:8080/swagger-ui/index.html#/
## Customer
+ get   
{host}/customer  
    -> retorna todos os customers 

{host}/customer?name=xyz 

    retorna todos customers que contenham "xyz"

{host}/customer/{id} 

    retorna o customer referente ao id 
Error: 

    Caso não exista o id, um erro 404 é retornado.

