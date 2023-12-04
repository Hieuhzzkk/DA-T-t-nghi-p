package vn.fs.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.fs.entities.InvoiceDetail;


@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail,Long>{
	
}
