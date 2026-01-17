// URL to fetch all products from Inventory service
const INVENTORY_PRODUCTS_URL = "http://localhost:8082/inventory/all";

// Container for product buttons
const productList = document.getElementById("productList");

// Fetch products from Inventory DB
function loadProducts() {
  fetch(INVENTORY_PRODUCTS_URL)
    .then(res => res.json())
    .then(products => {
      productList.innerHTML = ""; // Clear old buttons

      if (!products || products.length === 0) {
        productList.innerHTML = "<p>No products available</p>";
        return;
      }

      products.forEach(product => {
        const btn = document.createElement("button");
        btn.innerText = product.productName;
        btn.className = "product-item";
        btn.onclick = () => {
          // Go to product page with selected productId
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

// Initial load
loadProducts();

// Optional: reload products every 5-10 seconds to reflect DB changes
setInterval(loadProducts, 10000);
