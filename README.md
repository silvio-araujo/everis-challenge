# everis-challenge
Everis Contacts Challenge

## Proposta

Dada a seguinte estrutura de Entidade:
Agenda:
• Nome
• Data de Nascimento
• CPF
• Endereços:
o Tipo de Endereço
o Tipo de Logradouro
o Logradouro
o Numero
o Complemento
o Bairro
o Cidade
o CEP
• Telefones
o Tipo de Telefone
o DDI
o DDD
o Número (Regra de gravação de acordo com o Tipo de Telefone)
o Ramal
Desenvolver um microserviço que gerencie essas informações de forma persistente. Para tanto,
deve expor interface REST, no modelo HATEOAS, com os seguintes métodos:
• GET
• POST
• PUT
• DELETE
Requisitos
Obrigatórios
• Versão mínima do JRE: 11
• Utilização do framework SpringBoot (Versão mínima 2.2)
• Utilização de funções Lambda para manipulação de listas e mapas
• Utilização do framework de teste unitário JUnit 5 (com cobertura de 100% do
código)
• Utilização do SonarLint, onde a entrega do código não tenha nenhum Issue Critical,
Major ou Medium
• Javadoc de todos os pacotes, classes e métodos
• Parametrização por variáveis de ambiente
• Validação por REGEX
Desejáveis
• Uso de padrão de projeto CQRS como arquitetura de serviço
• Utilização de Cache
• Utilização de Filas de Mensageria
• Utilização de Banco de Dados NOSQL
• Utilização de técnicas de resiliência de serviço.

## Solução

Foi desenvolvido um microserviço (REST API no modelo HATEOAS), com os seguintes métodos:

### URL (GET): http://localhost:8080/contacts

Obtém todos os contatos cadastrados no banco

### URL (GET): http://localhost:8080/contacts/{contactId}/addresses

Obtém todos os endereços de um contato específico, dado seu ID

### URL (GET): http://localhost:8080/contacts/{contactId}/phones

Obtém todos os telefones de um contato específico, dado seu ID

### URL (GET): http://localhost:8080/contacts/{contactId}

Obtém um contato, dado seu ID

### URL (GET): http://localhost:8080/contacts/{contactId}/address/{addressId}

Obtém um endereço, dados o ID do seu dono e o próprio ID do endereço

### URL (GET): http://localhost:8080/contacts/{contactId}/phone/{phoneId}

Obtém um telefon, dados o ID do seu dono e o próprio ID do telefone

### URL (POST): http://localhost:8080/contacts

Adiciona um novo contato no banco, com JSON no corpo da requisição

### URL (POST): http://localhost:8080/contacts/{contactId}/address

Adiciona um novo endereço de um contato, dado o ID do contato, com JSON no corpo da requisição

### URL (POST): http://localhost:8080/contacts/{contactId}/phone

Adiciona um novo telefone de um contato, dado o ID do contato, com JSON no corpo da requisição

### URL (PUT): http://localhost:8080/contacts

Atualiza um contato, com JSON no corpo da requisição

### URL (PUT): http://localhost:8080/contacts/{contactId}/address

Atualiza um endereço, com JSON no corpo da requisição

### URL (PUT): http://localhost:8080/contacts/{contactId}/phone

Atualiza um telefone, com JSON no corpo da requisição

### URL (DELETE): http://localhost:8080/contacts/{contactId}

Remove um contato, dado seu ID

### URL (DELETE): http://localhost:8080/contacts/{contactId}/address/{addressId}

Remove um endereço, dados o ID do seu dono e seu próprio ID

### URL (DELETE): http://localhost:8080/contacts/{contactId}/phone/{phoneId}

Remove um telefone, dados o ID do seu dono e seu próprio ID

## Críticas

Os dados são criticados de acordo com a regra de negócios, só sendo aceitos informações no formato adequado para cada tipo de campo.
Para tal, foram usados REGEX correspondentes. Dados inválidos são recusados.

## Javadoc

O Javadoc foi utilizado para todos os pacotes, classes e métodos, conforme solicitado.

## Sonarlint

O Sonarlint foi passado, e algumas de suas sugestões foram acatadas e ajustadas, mas não todas. Algumas, por questão de legibilidade
de código, foram recusadas. Outras, de fato não houve tempo hábil para todos os ajustes serem feitos.

## Banco

O banco utilizado foi o H2. A intenção era de substituir pelo MongoDB após o término da implementação das funcionalidades requisitadas,
porém não houve tempo hábil para isso.

## Testes unitários

Não houve tempo hábil para implementá-los.

## Observações finais

Não conhecia várias das tecnologias aqui solicitadas nesse teste. Só a aplicação do HATEOAS já me tomou bastante tempo de aprendizado e
aplicação, mas consegui agregar a técnica no pouco tempo disponível.
