package kz.samgau.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "contract")
@Data
public class Item {
    @Id
    private int id;
    @Column(name = "contract_number")
    private String contract_number;
    @Column(name = "contract_number_sys")
    private String contract_number_sys;
    @Column(name = "trd_buy_id")
    private int trd_buy_id;
    @Column(name = "trd_buy_number_anno")
    private String trd_buy_number_anno;
    @Column(name = "ref_contract_type_id")
    private int ref_contract_type_id;
    @Column(name = "ref_contract_status_id")
    private int ref_contract_status_id;
    @Column(name = "crdate")
    private String crdate;
    @Column(name = "contract_sum_wnds")
    private double contract_sum_wnds;
    @Column(name = "supplier_id")
    private int supplier_id;
    @Column(name = "supplier_biin")
    private String supplier_biin;
    @Column(name = "customer_id")
    private int customer_id;
    @Column(name = "customer_bin")
    private String customer_bin;
    @Column(name = "index_date")
    private String index_date;
    @Column(name = "system_id")
    private int system_id;
}