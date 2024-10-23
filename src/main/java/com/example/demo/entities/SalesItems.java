package com.example.demo.entities;
import com.example.demo.Enum.Discount;
import com.example.demo.dto.SalesItemsDTO;
import javax.persistence.*;

@Entity
@Table
public class SalesItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sales_id", referencedColumnName = "id")
    private Sale sale;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "storage_id", referencedColumnName = "id")
    private StorageCenter storageCenter;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", columnDefinition = "varchar(50)", length = 50, nullable = true)
    private Discount discount;

    @Column(name = "quantity")
    private Long qnt;

    @Column(name = "product_value")
    private Long prodValue;

    @Column(name = "sub_total")
    private Double subTotal;

    public SalesItems() {
    }

    public SalesItems(SalesItemsDTO salesItemsDTO, Product product) {
        this.product = product;
        System.out.println("SalesItems" + salesItemsDTO);
        System.out.println("product" + product);
        System.out.println(product.getPrice() + salesItemsDTO.getDiscountValue() + salesItemsDTO.getQuantity());
        System.out.println("calculo: " + (product.getPrice()- salesItemsDTO.getDiscountValue()) + ((product.getPrice()- salesItemsDTO.getDiscountValue()) * salesItemsDTO.getQuantity()));
        this.subTotal = salesItemsDTO.getDiscountType() == Discount.DECIMAL ? ((product.getPrice() - salesItemsDTO.getDiscountValue()) * salesItemsDTO.getQuantity()) : ((product.getPrice() - product.getPrice() / 100 * salesItemsDTO.getDiscountValue()) * salesItemsDTO.getQuantity());
        this.discount = salesItemsDTO.getDiscountType();
        this.prodValue = product.getPrice();
        this.qnt = salesItemsDTO.getQuantity();
        this.storageCenter = salesItemsDTO.getStorageCenter();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sale getSales() {
        return sale;
    }

    public void setSales(Sale sales) {
        this.sale = sales;
    }

    public Product getProd() {
        return product;
    }

    public void setProd(Product prod) {
        this.product = prod;
    }

    public StorageCenter getStorageCenter() {
        return storageCenter;
    }

    public void setStorageCenter(StorageCenter storageCenter) {
        this.storageCenter = storageCenter;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Long getQnt() {
        return qnt;
    }

    public void setQnt(Long qnt) {
        this.qnt = qnt;
    }

    public Long getProdValue() {
        return prodValue;
    }

    public void setProdValue(Long prodValue) {
        this.prodValue = prodValue;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
