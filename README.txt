INTRUÇÕES PARA RODAR O NOSSO SISTEMA:
1. Rode o script (que está na pasta raiz do projeto) “ScriptDaPizzariaBD.sql” dentro do MYSQl (ou o MySQL Workbench) pra gerar as tabelas automaticamente. 

2. Coloque sua senha do MySQL local nesse caminho:
   src/main/java/br/edu/ufersa/PizzariaDAOeSERVICE/model/DAO/ConnectionManager.java
   private static final String PASS = "gadelhameajude"; <--- coloque a senha aqui.

3. No Painel do Maven expanda PizzariaDAOeService --> Puglins --> javafx --> javafx:run 

OBS: no meu computador havia erro de compilação por contado do nome de usuário do meu Windowns que pussia acento, se acontecer isso 
no seu PC, faça o seguinte:
- Se estiver usando Intellij aperte Ctrl + Alt + S
- na Barra esquerda expanda as pastas “Build, Execution, Deployment” --> “Build Tools” --> Maven(duplo clique)
- Na aba que abrir, vá até “Local repositor” marque “Override”
- Apague o camainho e coloque “C:\maven-repo”
- Aperte aply e ok.

Fluxo do Sistema:
- Crie uma conta proprietário (o sistema só cadastra 1 proprietário por vez).
- Login --> Crie alguns sabores de Pizza e de Adicional.
- A partir desse momento pode-se Registrar pedidos. 
