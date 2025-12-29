# 📚 ENEM – Corretor de Gabaritos

Aplicação web para **correção automática de provas do ENEM** a partir de PDFs oficiais de gabarito, retornando **nota final** e **estatísticas**.

---

## 🛠️ Tecnologias

- Java 17
- Spring Boot
- JUnit
- Docker
- Apache PDFBox

---

## 🎯 Propósito do projeto

Este projeto surgiu da necessidade prática de **automatizar a correção de simulados do ENEM**, eliminando correções manuais e erros humanos.

Apesar do foco no ENEM, a aplicação **pode funcionar com outros vestibulares**, desde que o formato do gabarito seja compatível.

Testado também com o gabarito da FATEC.

---

## ⚠️ Regras e limitações

- Provas **anteriores a 2011 não são suportadas**.
- O usuário deve fazer o download e fornecer o **PDF oficial do gabarito** da prova que deseja corrigir.
- A estrutura da prova varia conforme o ano de aplicação.
- Caso a prova seja de Linguagens:
    - Se `languageOption` não for informado, o sistema assume **INGLÊS** como padrão.

---

### Estrutura das provas

#### 2011 a 2016
- 1º dia: Ciências Humanas e Ciências da Natureza
- 2º dia: Linguagens e Matemática
- Questões **91 a 95**: língua estrangeira (inglês ou espanhol)

#### A partir de 2017
- 1º dia: Linguagens e Ciências Humanas
- 2º dia: Ciências da Natureza e Matemática
- Questões **1 a 5**: língua estrangeira (inglês ou espanhol)

---

## ⚙️ Funcionamento da aplicação

O usuário envia uma requisição contendo:
- O **PDF do gabarito**
- Suas **respostas**
- Opcionalmente:
    - Ano da prova
    - Língua estrangeira

O sistema:
1. Valida o PDF
2. Extrai o conteúdo bruto do PDF
3. Extrai o ano de prova, caso não informado na request
4. Valida o ano da prova
5. Extrai o gabarito do PDF
6. Corrige as respostas
7. Calcula a nota final

---

## 🔗 Endpoint – Correção de Prova

### `POST /api/v1/correct-exam`

Este endpoint recebe uma requisição **multipart/form-data**, contendo:
- um **arquivo PDF** do gabarito
- um **JSON** com as respostas do usuário

⚠️ **Importante:** não é um JSON puro. A requisição **obrigatoriamente** deve ser enviada como `form-data`.

---

## 📦 Formato da Request (Postman)

Ao utilizar o Postman, configure o **Body** como `form-data` e envie os seguintes campos:

| Key           |      | Value              | Content-Type       |
|---------------|------|--------------------|--------------------|
| `file`        | File | nome_gabarito.pdf  | `application/pdf`  |
| `userAnswers` | Text | JSON com respostas | `application/json` |

---

### 🗂️ Campo `file`

- Tipo: **File**
- Content-Type: **`application/pdf`**
- Deve conter o **PDF do gabarito oficial**
- Exemplo: `2025_GB_D2_CD8.pdf`

---

### 🧾 Campo `userAnswers`

- Tipo: **Text**
- Content-Type: **`application/json`**
- Deve conter um JSON no seguinte formato:

### Exemplo 1 – Linguagem e ano de prova informados

```json
{
  "languageOption": "ESPANHOL",
  "manualExamYear": 2026,
  "answers": {
    "1": "A",
    "2": "B",
    "3": "C",
    "4": "D",
    "5": "E"
  }
}
```
---

### Campos

- `languageOption`: `INGLES`, `ESPANHOL` ou `null`
- `manualExamYear`: mínimo **2011**, máximo **ano atual + 1**
- `answers`: questões de **1 a 180**, respostas de **A a E**

---

### Exemplo 2 - Linguagem e ano de prova não informados
```json
{
    "languageOption": null,
    "manualExamYear": null,
    "answers": {
        "91": "A",
        "92": "B",
        "93": "C",
        "94": "D",
        "95": "E"
  }
}
```

---

## 📤 Response

A resposta retorna um resumo completo da correção, incluindo quantidade de acertos,
erros, questões anuladas e o gabarito esperado.

### Exemplo de Response

```json
{
  "correctCount": 1,
  "wrongCount": 2,
  "totalAnswered": 3,
  "totalQuestions": 89,
  "totalCanceled": 1,
  "wrongAnswers": {
    "91": "C",
    "92": "D"
  },
  "expectedAnswers": {
    "91": "E",
    "92": "E"
  },
  "cancelledQuestions": {
    "132": "Anulado"
  }
}
```

| Campo                | Tipo    | Descrição                                              |
| -------------------- | ------- |--------------------------------------------------------|
| `correctCount`       | Integer | Total de questões respondidas corretamente             |
| `wrongCount`         | Integer | Total de questões respondidas incorretamente           |
| `totalAnswered`      | Integer | Total de questões respondidas pelo usuário             |
| `totalQuestions`     | Integer | Total de questões consideradas na prova                |
| `totalCanceled`      | Integer | Total de questões anuladas                             |
| `wrongAnswers`       | Object  | Respostas informadas pelo usuário que estão incorretas |
| `expectedAnswers`    | Object  | Respostas contendo a alternativa correta               |
| `cancelledQuestions` | Object  | Questões anuladas                                      |

---

## 🌐 Aplicação em produção

A API está disponível em: 👉 https://enem-icih.onrender.com

- Base URL: https://enem-icih.onrender.com
- Endpoint (POST): `/api/v1/correct-exam`

---

## 🖥️ Front end

- Aplicação: https://enem-front.vercel.app/
- Documentação: https://github.com/devcarlosdaniel0/enem-front
