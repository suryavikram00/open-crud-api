// package com.api.open.crud.api.entity.user_micro;

// /*
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//  */


// import com.api.open.crud.api.entity.BaseEntity;
// import java.io.Serializable;
// import java.util.List;
// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;
// import javax.persistence.MappedSuperclass;
// import javax.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.NaturalId;

// /**
//  *
//  * @author NMSLAP570
//  */
// // @Entity
// @AllArgsConstructor
// @NoArgsConstructor
// // @Table(name = "user")
// @Data
// @MappedSuperclass
// public class UserEntityModel extends BaseEntity<Long>
//         implements Serializable {

//     private static final long serialVersionUID = 1L;
//     @Id
//     @Column(name = "id")
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
    
//     @Column(name = "email")
//     @NaturalId
//     private String email;
    
    
//     @Column(name = "username")
//     private String username;


//     @Override
//     public List<String> getTableHeaderNames() {
//         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//     }
    

//     @Override
//     public String updatedByColumn() {
//         return "";
//     }

//     @Override
//     public String updatedOnColumn() {
//         return "";
//     }

//     @Override
//     public String createdByColumn() {
//         return "";
//     }

//     @Override
//     public String createdOnColumn() {
//         return "";
//     }

// }
