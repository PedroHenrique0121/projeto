package br.ueg.posse.si.p4.test.modelo;



import br.ueg.posse.si.p4.annotationsCustomizadas.Column;
import br.ueg.posse.si.p4.annotationsCustomizadas.Entity;
import br.ueg.posse.si.p4.annotationsCustomizadas.Id;

/*Toda <Classe> Modelo que estiver presente esta anotação @entity irá refletir uma entidade no banco de dados;
  Por enquanto não de forma automatica, mas por meio de verificação e terá devido tratamento em tempo de execução;
*/
@Entity
public class Produto {
    
    /*Todo atributo que estiver presente esta anotação @Id(-) refletirá uma chave primaria na entidade 
    respectiva de sua classe
    Parametros? SIM:  GenerationTye -> tipo de geração de chave : Id.AUTO -> AUTO INCREMENT
    de fato se não aver anotação equivalente a @Id entidade não possuira chave primaria;
    
    OBS segunda etapa Verificação de mudanças  : pode ser que eu consiga fazer com que possa alterar o que foi dito 
    na linha 18;
    */
   @Id(GenerationType = Id.AUTO)
    private int id;
    
   /*NÃO NECESSARIO, porem sua utilidade é baseada na necessidade de alterar o valor 
    da quantidade de caacterer padrão VARCHAR(250) */
    @Column(value = 99)
    private String nome;
    
    @Column(value = 100 ) 
    private String descktop;
    private double preco;
    private int estoque;

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getDesc() {
        return descktop;
    }

    public void setDesc(String desc) {
        this.descktop = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

}
