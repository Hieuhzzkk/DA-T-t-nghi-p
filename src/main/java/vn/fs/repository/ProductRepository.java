package vn.fs.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.fs.entities.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


	// List product by category
	@Query(value = "SELECT * FROM products WHERE category_id = ?", nativeQuery = true)
	public List<Product> listProductByCategory(Long categoryId);

	@Query(value = "SELECT * FROM products WHERE idhang = ?", nativeQuery = true)
	public List<Product> listProductByHang(Long idhang);
//	@Query(value = "select * from products where hang = ? and category_id = ? and price >= 250000 and price <= 600000;", nativeQuery = true)
//	public List<Product> listProductBygia(String hang, Long categoryId, Double price);
	
//	@Query(value = "SELECT * FROM products WHERE price between :min and :max")
//	public List<Product> searchByPrice(@Param("min") double min,@Param("max") double max);
	
	@Query(value = "SELECT * FROM products WHERE size_id = ?", nativeQuery = true)
	public List<Product> listProductBySize(Long idSize);
	
	@Query(value = "SELECT * FROM products WHERE size_id = ?2 and masp = ?1", nativeQuery = true)
	public Product ProductBySize2(int masp,Long idSize);
	
	@Query(value = "SELECT * FROM products WHERE masp = ?", nativeQuery = true)
	public List<Product> listProductByMaSP(int masp);
	@Query(value = "SELECT * FROM products WHERE masp = ? and size_real = ?", nativeQuery = true)
	public List<Product> listProductByMaSP2(int masp, String sizeReal);
	// Top 10 product by category
	@Query(value = "SELECT * FROM products AS b WHERE b.category_id = ?;", nativeQuery = true)
	List<Product> listProductByCategory10(Long categoryId);
	
	// List product new
	@Query(value = "SELECT * FROM products ORDER BY entered_date DESC limit 20;", nativeQuery = true)
	public List<Product> listProductNew20();
	
	// Search Product
	@Query(value = "SELECT * FROM products WHERE product_name LIKE %?1%" , nativeQuery = true)
	public List<Product> searchProduct(String productName);
	
	// count quantity by product
	@Query(value = "SELECT c.category_id, c.category_name, COUNT(*) AS SoLuong  \r\n"
			+ "FROM products p  \r\n"
			+ "JOIN categories c ON p.category_id = c.category_id  \r\n"
			+ "GROUP BY c.category_id, c.category_name;" , nativeQuery = true)
	List<Object[]> listCategoryByProductName();
	
	// Top 20 product best sale
	@Query(value = "SELECT p.product_id,\r\n"
			+ "COUNT(*) AS SoLuong\r\n"
			+ "FROM order_details p\r\n"
			+ "JOIN products c ON p.product_id = c.product_id\r\n"
			+ "GROUP BY p.product_id\r\n"
			+ "ORDER by SoLuong DESC limit 20;", nativeQuery = true)
	public List<Object[]> bestSaleProduct20();
	
	@Query(value = "select * from products o where product_id in :ids", nativeQuery = true)
	List<Product> findByInventoryIds(@Param("ids") List<Integer> listProductId);
	
	@Query(value = "select * from products where "
			+ " (?3 is null or (price<=?3 and ?3 >0)) "
			+ " and (?2 is null or hang = ?2) "
			+ " and (?1 is null or category_id = ?1) ", nativeQuery = true)
	Page<Product> search(Integer idCate,String hang,Long price,Pageable pageable);
	@Query(value = "SELECT p FROM Product p " +
	        "JOIN Hang h ON p.idhang = h.idhang " +
	        "JOIN Category c ON p.category_id = c.category_id " +
	        "WHERE c.category_id = ?1 AND h.idhang = ?2 AND p.price = ?3",nativeQuery = true)
	List<Product> productByCateBranPrice(Long idCate, Long idHang, Double price);

}
