# EDA - Estrutura de Dados Avançado

Temas estudados na disciplina de Estrutura de Dados Avançado, usando a linguagem Java:

- Graphs
- Heap
- HashMap
- Trees
    + BST
    + AVL
    + Red-Black
    + B-Tree

## Graphs

Um gráfo é uma estrutura de dados não linear que consiste em nós e arestas. Os nós às vezes também são chamados de vértices e as arestas são linhas ou arcos que conectam quaisquer dois nós no gráfo.

## Heap

Basicamente é uma BST armazenada em um vetor/array. Dessa forma, fazemos o mapeamento dos elementos do vetor, em uma BST para um elemento na posição `i`, da seguinte maneira:
- O elemento na posição `2i`, é o seu filho esquerdo;
- O elemento na posição `2i + 1`, é o seu filho direito;
- O elemento na posição `⌊i/2⌋`, é o seu pai;


## HashMap

Estrutura que utiliza do conceito de *Hash* para armazenar o elemento.

## Trees

### BST (Binary Search Tree)

Estrutura de dado representada por nós, no qual cada nó guarda uma chave, e tem referências para outros dois nós (sendo eles nulos ou não). Uma de suas características é a facilidade na ação de buscar, por causa de sua ordenação de elementos. Além disso, se for uma árvore com chaves aleatórias uniformemente distribuídas, então ela téra altura `O(log n)`.

### AVL Tree

Estrutura de árvores altamente balanceada, isto é, nas inserções e exclusões, procura-se executar uma rotina de balanceamento tal que as alturas das sub-árvores esquerda e sub-árvores direita tenham alturas bem próximas.

### Red-Black Tree

Uma *BST rubro-negra*, é uma BST que simula uma *árvore 2-3*. Cada nó duplo da árvore 2-3 é representado por dois nós simples ligados por um link rubro. Nossas BSTs são esquerdistas (left-leaning), pois os links rubros são sempre inclinados para a esquerda. 
Dessa forma, podemos defini-la como uma BST cujos links são negros ou rubros e têm as seguintes popriedades:
- links rubros se inclinam para a esquerda;
- nenhum nó incide em dois links rubros;
- balanceamento negro perfeito: todo caminho da raiz até um link null tem o mesmo número de links negros

### B-Tree: 

Estrutura de árvore de ordem N, com as seguintes propriedades:
- cada nó contém no máximo M−1 chaves,
- a raiz contém no mínimo 2 chaves e cada um dos demais nós contém no mínimo M/2 chaves,
- cada nó que não seja uma folha tem um filho para cada uma de suas chaves,
- todos os caminhos da raiz até uma folha têm o mesmo comprimento (ou seja, a árvore é perfeitamente balanceada).
