# compiladores
Trabalho de compiladores. CEFET-MG TIMOTEO 1T/2019

O projeto consiste em duas aplicações distintas:
	- Chatbot-api: contem o backend da aplicação, contendo o compilador da disciplina;
	- Chatbot-ui: contem o frontend da aplcação, contendo apenas uma interface mais amigável entre homem/máquina;

-API: o código fonte está organizado dentro do diretorio "Chatbot-api/src/main/java/com/influenzer". Um arquivo compilado pode ser
 encontrado no diretório "Chatbot-api/target/chatbot.jar" contendo o arquivo compilado do backend.

-UI: para a execucao da interface, é necessário seguir os seguintes passos:
	- instalar o NodeJs;
	- abrir o diretorio "chatbot-ui" e executar o comando "npm install";
	- dentro do diretorio "chatbot-ui", execute o comando "ng serve";

Gramática formal:

Pgt ::= Como {servico} {software} ? | Como {servico} {equipamento} ?
	
Pgt ::= Gostaria {expressao}
	
Pgt ::= Preciso {expressao}

Attr ::= {substantivo} é {modelo} | {substantivo} esta {modelo} | {categoria-informacao} é {informacao} | {equipamento} e {fabricante} | {software} e {fabricante}
	
Attr ::= {defeito}
	
Attr ::= Sim

Attr ::= Não

Attr ::= {substantivo} {defeito}
	
Attr ::= está com {tipo-defeito}

Attr ::= Obrigado | Endendi

{substantivo} ::= {equipamento} | {software} | {fabricante}
	
{expressao} ::= {servico} {equipamento} | {servico} {software} | {equipamento} {defeito}
	
{adjetivo} ::= {defeito} | {informacao}

{servico} ::= Consertar | Instalar | Saber | Descobrir | Verificar

{defeito} :: não {tipo-defeito} 
	
{tipo-defeito} ::= Liga | Funciona | Inicializa | Acende | Executa | PROBLEMA

{categoria-informacao} ::= cpf | cnpj | pedido
	
{informacao} ::= CPF | CNPJ | DATA COMPRA

{fabricante} ::= DELL | APPLE | MICROSOFT | LENOVO | LG | GOOGLE
	
{software} ::= DRIVER | PROGRAMA | WINDOWS | MACOS | LINUX | UBUNTU
	
{modelo} ::= BRANCO | PRETO | CINZA | VERMELHO | NUMERO | WINDOWS | MACOS | LINUX | UBUNTU
	
{equipamento} ::= COMPUTADOR | NOTEBOOK | CELULAR | IMPRESSORA | MOUSE | MONITOR | TELA
 
