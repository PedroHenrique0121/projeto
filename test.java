if (objeto instanceof Produto)
    Produto produto = (Produto)objeto;
    String nome = produto.getNome();
    double valor = produto.getValor();
    String marca = produto.getMarca();

    xml = "<produto>" +
            "<nome>" + nome + "</nome>" +
            "<valor>" + valor + "</valor>" +
            "<marca>" + marca + "</marca>" +
        "</produto>";
}else if (objeto instanceof Cliente) {
    // lógica para gerar o XML
} //o