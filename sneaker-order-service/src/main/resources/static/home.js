const INVENTORY_PRODUCTS_URL = "http://localhost:8082/inventory/all";

const productList = document.getElementById("productList");

function loadProducts() {
  fetch(INVENTORY_PRODUCTS_URL)
    .then(res => res.json())
    .then(products => {
      productList.innerHTML = ""; 

      if (!products || products.length === 0) {
        productList.innerHTML = "<p>No products available</p>";
        return;
      }

      products.forEach(product => {
        const btn = document.createElement("button");
        btn.innerText = product.productName;
        btn.className = "product-item";
        btn.onclick = () => {
          window.location.href = `product.html?productId=${product.id}`;
        };
        productList.appendChild(btn);
      });
    })
    .catch(err => {
      console.error("Failed to load products", err);
      productList.innerHTML = "<p>Error loading products</p>";
    });
}

loadProducts();

setInterval(loadProducts, 10000);
