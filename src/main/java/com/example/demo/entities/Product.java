package com.example.demo.entities;
import com.example.demo.dto.ProdutoNewDTO;
import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product extends DefaultEntities {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "bar_code", unique = true)
    private String barCode;

    @Column(name = "current_stock")
    private Long currentStock;

    @Column(name = "price")
    private Long price;

    @Column(name = "negative_stock")
    private Integer negativeStock;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private Type type;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "mu_id", referencedColumnName = "id")
    private MU mu;

    public Product () {}

    public Product (ProdutoNewDTO prodDTO) {

        this.name = prodDTO.getName();
        this.description = prodDTO.getDescription();
        this.barCode = prodDTO.getBarCode();
        this.active = prodDTO.getActive() ? 1 : 0;
        this.mu = prodDTO.getMu();
        this.brand = prodDTO.getBrand();
        this.group = prodDTO.getGroup();
        this.type = prodDTO.getType();
        this.killed = prodDTO.getKilled() ? 1 : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brand getBrand() {
        return brand;
    }

    public Long getBrandId() {
        if(brand != null){
        return brand.id;
        } else {
            return 0L;
        }
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Group getGroup() {
        return group;
    }

    public Long getGroupId() {
        if(group != null){
            return group.id;
        } else {
            return 0L;
        }
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Type getType() {
        return type;
    }

    public Long getTypeId() {
        if(type != null){
            return type.id;
        } else {
            return 0L;
        }
    }

    public void setType(Type type) {
        this.type = type;
    }

    public MU getMu() {
        return mu;
    }

    public void setMu(MU mu) {
        this.mu = mu;
    }

    public Long getMUid() {
        if(mu != null){
            return mu.id;
        } else {
            return 0L;
        }
    }

    public Long getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Long currentStock) {
        this.currentStock = currentStock;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNegativeStock() {
        return negativeStock;
    }

    public void setNegativeStock(Integer negativeStock) {
        this.negativeStock = negativeStock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "killed=" + killed +
                ", active=" + active +
                ", id=" + id +
                ", mu=" + mu +
                ", type=" + type +
                ", group=" + group +
                ", brand=" + brand +
                ", barCode='" + barCode + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", currentStock='" + currentStock + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
