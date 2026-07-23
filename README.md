# Vila do Bitcoin 🏘️₿

Jogo educativo web para ensinar Bitcoin para crianças, construído com Spring Boot + Thymeleaf + HTMX.

## Stack

| Camada | Tecnologia |
|---|---|
| Backend | Spring Boot 3.2 + Kotlin |
| Templates | Thymeleaf (HTML no servidor) |
| Interatividade | HTMX (sem JavaScript manual) |
| Banco de dados | H2 (em memória) |
| Deploy | Railway (gratuito) |

---

## Rodando localmente

### Pré-requisitos
- JDK 17+
- (opcional) IntelliJ IDEA

### Pelo terminal

```bash
# clonar / entrar na pasta
cd bitcoin-game

# rodar
./gradlew bootRun
```

Acesse: **http://localhost:8080**

### Pelo IntelliJ
1. Abrir a pasta `bitcoin-game`
2. Aguardar o Gradle indexar
3. Rodar `BitcoinGameApplication.kt` (botão ▶️)

---

## Deploy gratuito no Railway

### 1. Subir para o GitHub
```bash
git init
git add .
git commit -m "primeiro commit"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/bitcoin-game.git
git push -u origin main
```

### 2. Criar projeto no Railway
1. Acesse [railway.app](https://railway.app) e faça login com GitHub
2. Clique em **New Project → Deploy from GitHub repo**
3. Selecione o repositório `bitcoin-game`
4. Railway detecta Spring Boot automaticamente e faz o build

### 3. Configurar variável de porta
No painel Railway → seu projeto → **Variables**:
```
PORT=8080
```

### 4. Pronto!
Railway gera uma URL pública como `https://bitcoin-game-production.up.railway.app`

---

## Estrutura do projeto

```
src/main/kotlin/com/bitcoingame/
├── BitcoinGameApplication.kt     # entrada da aplicação
├── controller/
│   └── GameController.kt         # rotas HTTP
├── service/
│   └── GameService.kt            # lógica das fases
└── model/
    ├── Player.kt                  # entidade JPA
    ├── PlayerRepository.kt        # repositório
    └── Phase.kt                   # data classes auxiliares

src/main/resources/
├── templates/
│   ├── index.html                 # tela inicial
│   ├── game.html                  # página principal do jogo
│   └── fragments/
│       ├── phase1.html            # escambo
│       ├── phase2.html            # inflação
│       ├── phase3.html            # banco
│       ├── phase4.html            # bitcoin
│       ├── barter-result.html     # resultado da troca
│       └── mint-result.html       # resultado da cunhagem
└── static/css/
    └── game.css                   # estilos
```

---

## Como funciona o HTMX

O HTMX é uma biblioteca minúscula que você inclui com uma linha no HTML:
```html
<script src="https://unpkg.com/htmx.org@1.9.12"></script>
```

Depois é só adicionar atributos nos botões:
```html
<button hx-post="/game/1/barter"
        hx-target="#barter-area"
        hx-swap="innerHTML">
    Trocar!
</button>
```

Quando o usuário clica, o HTMX faz um POST para `/game/1/barter` e substitui o conteúdo de `#barter-area` com o HTML que o servidor retorna. Sem JavaScript, sem React, sem Vue — só Kotlin!

---

## Próximos passos sugeridos

- [ ] Adicionar sistema de pontuação e ranking
- [ ] Criar mais fases (carteira digital, chaves públicas/privadas)
- [ ] Adicionar sons e animações via CSS
- [ ] Internacionalizar (inglês, espanhol)
- [ ] Salvar progresso com sessão ou login simples
- [ ] Migrar H2 para PostgreSQL para persistência real
