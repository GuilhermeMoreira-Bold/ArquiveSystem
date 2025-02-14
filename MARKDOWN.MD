# Documentação do Sistema de Arquivos Simulado em Java

Este projeto implementa um sistema de arquivos simples em Java, com base em FAT (File Allocation Table) para gerenciar a alocação de blocos em um disco simulado. O sistema de arquivos simulado oferece funcionalidades para gerenciar diretórios, arquivos e operações básicas de leitura e escrita.

---

## Funcionalidades do Sistema de Arquivos

### 1. **Criação do Disco e Inicialização**
O sistema de arquivos simula um disco rígido, onde os dados são armazenados em blocos de tamanho fixo. Quando o disco é inicializado, ele pode ser criado do zero ou restaurado a partir de um estado pré-existente.

- **Disco Novo**: Caso o disco seja novo, a FAT e a área de dados são inicializadas com valores padrão.
- **Disco Existente**: Caso o disco já exista, os dados existentes são carregados para permitir o gerenciamento de arquivos e diretórios.

### 2. **Gestão de Diretórios**
O sistema permite a criação e manipulação de diretórios. Cada diretório pode conter arquivos e outros subdiretórios.

- **Criação de Diretórios**: A classe `Directory` permite a criação de diretórios, com a habilidade de adicionar e remover subdiretórios.
- **Caminho Completo**: O sistema gera automaticamente o caminho completo de qualquer diretório, incluindo o diretório raiz e todos os seus pais.
- **Exibição de Diretórios**: A classe `Directory` fornece métodos para listar os arquivos e subdiretórios contidos em um diretório.

### 3. **Gestão de Arquivos**
Os arquivos são armazenados como entradas em diretórios e são alocados no disco de acordo com a tabela FAT.

- **Criação de Arquivos**: Arquivos podem ser criados com dados associados a eles, como nome, conteúdo e tamanho.
- **Manipulação de Dados de Arquivos**: O conteúdo dos arquivos pode ser lido e alterado por meio de operações de leitura e escrita.
- **Tamanho e Blocos**: Cada arquivo tem seu tamanho em blocos de dados e seu bloco inicial de armazenamento no disco.

### 4. **Tabela de Alocação de Arquivos (FAT)**
O sistema utiliza a tabela FAT para gerenciar a alocação de blocos no disco. A FAT mapeia os clusters usados por arquivos e diretórios, indicando os clusters livres e ocupados.

- **Leitura e Escrita na FAT**: O sistema pode ler e escrever dados na tabela FAT, tanto em blocos específicos quanto em toda a tabela.
- **Gerenciamento de Blocos**: A FAT mantém o controle de quais blocos estão ocupados por arquivos e quais estão livres, ajudando na alocação e liberação de espaço.

### 5. **Área de Dados**
Os dados dos arquivos são armazenados em uma área separada no disco. Cada arquivo ocupa uma quantidade específica de blocos na área de dados.

- **Leitura e Escrita de Dados**: A área de dados pode ser lida e escrita diretamente, e os dados dos arquivos podem ser manipulados de acordo com as operações de entrada/saída solicitadas.
- **Clusters**: O disco é dividido em clusters, que são unidades de armazenamento que contêm os dados dos arquivos. Cada cluster possui um tamanho fixo e os arquivos são distribuídos entre esses clusters.

### 6. **Operações de Arquivos e Diretórios**
O sistema de arquivos simulado permite uma série de operações padrão para gerenciamento de arquivos e diretórios:

- **Criação de Arquivos**: Permite criar novos arquivos dentro de um diretório, alocando blocos e preenchendo a FAT.
- **Leitura de Arquivos**: Arquivos podem ser lidos de qualquer parte do disco, através da leitura de blocos específicos.
- **Escrita em Arquivos**: O conteúdo dos arquivos pode ser alterado, substituindo dados em blocos específicos.
- **Listagem de Diretórios**: O sistema pode listar todos os arquivos e subdiretórios de um diretório específico.
- **Deleção de Arquivos e Diretórios**: Arquivos e diretórios podem ser removidos do sistema, liberando os blocos associados a eles e atualizando a FAT.

### 7. **Estrutura de Diretórios**
O sistema de arquivos é hierárquico, permitindo a criação de diretórios dentro de outros diretórios. Cada diretório pode ter uma estrutura de subdiretórios e arquivos.

- **Subdiretórios**: Diretórios podem ser aninhados, permitindo a criação de árvores de diretórios.
- **Raiz e Pais**: Cada diretório possui uma referência ao seu diretório pai, e todos os diretórios estão conectados a partir de um diretório raiz.

### 8. **Funcionalidades de Leitura e Escrita**
O sistema fornece várias operações de leitura e escrita tanto para os dados de arquivos quanto para os metadados (como entradas de diretórios e FAT).

- **Leitura de Arquivos**: Os arquivos podem ser lidos em blocos de dados, permitindo que o conteúdo seja acessado.
- **Escrita em Arquivos**: Os arquivos podem ser modificados escrevendo novos dados nos blocos alocados.
- **Leitura de Diretórios**: O sistema pode ler diretórios e listar seus arquivos e subdiretórios.
- **Escrita de Diretórios**: Novos arquivos e subdiretórios podem ser adicionados aos diretórios existentes.

---

## Fluxo de Funcionamento do Sistema de Arquivos

1. **Inicialização do Disco**: Quando o sistema de arquivos é carregado, o disco pode ser inicializado a partir de um arquivo existente ou criado do zero.
2. **Manipulação de Diretórios**: Usuários podem criar diretórios e subdiretórios, navegar através da estrutura de diretórios e listar seus conteúdos.
3. **Criação e Manipulação de Arquivos**: Arquivos podem ser criados, escritos e lidos. Quando criados, eles são alocados em blocos de dados e registrados na tabela FAT.
4. **Gerenciamento de Blocos e FAT**: A tabela FAT gerencia a alocação e liberação de blocos de dados, e a área de dados armazena os conteúdos dos arquivos.
5. **Desalocação de Arquivos e Diretórios**: Quando arquivos ou diretórios são removidos, a FAT é atualizada para liberar os blocos, e os dados no disco podem ser apagados.

---

## Conclusão

O sistema de arquivos simulado é uma implementação robusta para a gestão de arquivos e diretórios, utilizando conceitos de alocação de blocos e estrutura hierárquica de diretórios. Ele pode ser expandido para incluir funcionalidades adicionais, como suporte a permissões de acesso, criptografia ou sistemas de recuperação de dados. A utilização da tabela FAT permite uma alocação eficiente de espaço no disco, tornando o sistema de arquivos fácil de entender e gerenciar.

---
