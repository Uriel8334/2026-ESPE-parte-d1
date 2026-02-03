-------
USUARIO

Programa el siguiente codigo para java confeccionando solamente las pruebas unitarias, de forma limpia y ordenada, sin emoticones en los comentarios de impresion de pantalla ni dentro del codigo. 
Recuerda que solamente se puede modificar el OrderTest.java, basandose en el codigo que ya se encuentra disponible, las pruebas unitarias pueden ser realizadas en base a lo que se tiene disponible.
El codigo ya implementado es el siguiente:

-- Item.java 
package es.upm.grise.profundizacion.order;

public interface Item {
    
    Product getProduct();
    int getQuantity();
    void setQuantity(int i);
    double getPrice();

}


-- Order.java
package es.upm.grise.profundizacion.order;

import java.util.ArrayList;
import java.util.Collection;

import es.upm.grise.exceptions.IncorrectItemException;

public class Order {

    private Collection<Item> items;

    /*
     * Constructor
     */
    public Order() {
        
        this.items = new ArrayList<Item>();
        
    }

    /*
     * Method to test
     */
    public void addItem(Item item) throws IncorrectItemException {

        if (item.getPrice() < 0) {
            
            throw new IncorrectItemException();
            
        }

        if (item.getQuantity() <= 0) {
            
            throw new IncorrectItemException();
            
        }

        for (Item i : items) {
            
            if (i.getProduct().equals(item.getProduct())) {
                
                if (Double.compare(i.getPrice(), item.getPrice()) == 0) {
                    
                    i.setQuantity(i.getQuantity() + item.getQuantity());
                    return;

                } else {
                    
                    items.add(item);
                    return;
                    
                }
            }
        }

        items.add(item);
    }
    
    /*
     * Setters/getters
     */
    public Collection<Item> getItems() {
        
        return this.items;
        
    }

}

-- Product.java
package es.upm.grise.profundizacion.order;

public class Product {
    
    // Please notice the difference between the class diagram and this implementation
    // The reason is to facilitate unit testing
    
    long id;
    
    void setId(long id) {
        
        this.id = id;
        
    }
    
    long getId() {
        
        return id;
        
    }

}

-- OrderTest.java 
package es.upm.grise.profundizacion.order;

import org.junit.jupiter.api.Test;

public class OrderTest {
    
    @Test
    public void smokeTest() {}

}

Las indicaciones son las siguientes:

"Especificación de la clase Order
 
Arquitectura del sistema
La  clase Order contiene los artículos comprados por un cliente de un portal de compras.

Figura 1. Diagrama de clases
 
La arquitectura del sistema de software se muestra en Figura 1. El portal de compras vende una amplia gama de productos. Cuando un cliente quiere hacer una compra, se crea una nueva order. Dicha instanciación la realiza una clase controladora que no se muestra en el diagrama. 
Cada vez que un cliente selecciona un product, se crea un nuevo item[1] y la clase controladora añade el item a la order.  El portal de compras contiene productos de diferentes proveedores, y cada proveedor puede asignar un precio distinto para un product. 
 

 
Implementación requerida de la clase Order 
 
La  clase Order toma los siguientes valores durante la instanciación:
La lista de items deberá estar vacía, pero no nula.
 
Se deben implementar los siguientes métodos de la clase Order: 
 
void addItem(Item item)
Este método añade un item a la order
El item se añade a la lista de items. 
El price del item deberá ser mayor o igual a cero. De lo contrario, se lanzará una IncorrectItemException.
La quantity del item será mayor que cero. De lo contrario, se lanzará una IncorrectItemException.
Si el product ya existe en la lista de items, el nuevo item no se añade, sino que se incrementa la quantity del  item existente en la lista de items.
Si el product ya existe en la lista de items, pero el price varía, el nuevo item se añade a la lista de items (imagine, por ejemplo, el product vendido por dos proveedores diferentes con precios distintos).
 

[1] Ten en cuenta que la relación entre Order y Product es n:m, por lo que se requiere un objeto intermedio." 
--------------
GEMINI 3 PRO 

Aquí tienes el código para OrderTest.java.

Se ha implementado utilizando JUnit 5 y Mockito, que es el estándar profesional para realizar pruebas unitarias cuando se depende de interfaces (como Item). Se ha diseñado para cumplir con la arquitectura limpia y cubrir todos los casos especificados en los requerimientos.

package es.upm.grise.profundizacion.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.upm.grise.exceptions.IncorrectItemException;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    private Order order;
    
    @Mock
    private Item item1;
    
    @Mock
    private Item item2;
    
    @Mock
    private Product product;

    @BeforeEach
    public void setUp() {
        // Inicializamos una orden vacia antes de cada prueba
        order = new Order();
    }

    @Test
    public void testOrderItemsCollectionIsInitiallyEmpty() {
        // Verificamos que la lista se inicializa vacia pero no nula
        assertNotNull(order.getItems(), "La lista de items no deberia ser nula");
        assertTrue(order.getItems().isEmpty(), "La lista de items deberia estar vacia al inicializar");
    }

    @Test
    public void testAddItemThrowsExceptionWhenPriceIsNegative() {
        // Configuramos el mock para devolver un precio negativo
        when(item1.getPrice()).thenReturn(-1.0);

        // Verificamos que lanza la excepcion
        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(item1);
        }, "Deberia lanzar IncorrectItemException si el precio es negativo");
    }

    @Test
    public void testAddItemThrowsExceptionWhenQuantityIsZero() throws IncorrectItemException {
        // Configuramos precio valido pero cantidad cero
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(0);

        // Verificamos que lanza la excepcion
        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(item1);
        }, "Deberia lanzar IncorrectItemException si la cantidad es cero");
    }

    @Test
    public void testAddItemThrowsExceptionWhenQuantityIsNegative() throws IncorrectItemException {
        // Configuramos precio valido pero cantidad negativa
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(-5);

        // Verificamos que lanza la excepcion
        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(item1);
        }, "Deberia lanzar IncorrectItemException si la cantidad es negativa");
    }

    @Test
    public void testAddItemAddsNewItemWhenListIsEmpty() throws IncorrectItemException {
        // Configuramos un item valido
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(1);
        
        // Ejecucion
        order.addItem(item1);

        // Verificacion
        assertEquals(1, order.getItems().size(), "El item deberia haberse anadido a la lista");
        assertTrue(order.getItems().contains(item1), "La lista deberia contener el item especifico");
    }

    @Test
    public void testAddItemIncrementsQuantityWhenProductAndPriceAreSame() throws IncorrectItemException {
        // Escenario: El mismo producto con el mismo precio se anade dos veces.
        // Se espera que no se anada un nuevo elemento, sino que se sume la cantidad al existente.

        // Configuracion Item 1 (Existente)
        when(item1.getProduct()).thenReturn(product);
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(5);

        // Configuracion Item 2 (Nuevo intento de adicion)
        when(item2.getProduct()).thenReturn(product); // Mismo objeto producto
        when(item2.getPrice()).thenReturn(10.0);      // Mismo precio
        when(item2.getQuantity()).thenReturn(3);

        // Ejecucion
        order.addItem(item1);
        order.addItem(item2);

        // Verificaciones
        // 1. La lista sigue teniendo solo 1 elemento
        assertEquals(1, order.getItems().size(), "No se deberia anadir un nuevo elemento a la lista");
        
        // 2. Se debe haber llamado a setQuantity en el item1 con la suma (5 + 3 = 8)
        // Nota: Order.java hace: i.setQuantity(i.getQuantity() + item.getQuantity());
        verify(item1).setQuantity(8);
    }

    @Test
    public void testAddItemAddsNewEntryWhenProductIsSameButPriceIsDifferent() throws IncorrectItemException {
        // Escenario: El mismo producto pero con diferente precio (diferente proveedor).
        // Se espera que se anada como un item separado.

        // Configuracion Item 1
        when(item1.getProduct()).thenReturn(product);
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(1);

        // Configuracion Item 2 (Mismo producto, precio distinto)
        when(item2.getProduct()).thenReturn(product);
        when(item2.getPrice()).thenReturn(12.5); 
        when(item2.getQuantity()).thenReturn(1);

        // Ejecucion
        order.addItem(item1);
        order.addItem(item2);

        // Verificaciones
        assertEquals(2, order.getItems().size(), "Se deberian tener dos elementos distintos debido a la diferencia de precio");
    }

    @Test
    public void testAddItemAddsNewEntryWhenProductIsDifferent() throws IncorrectItemException {
        Product productB = mock(Product.class);

        // Configuracion Item 1
        when(item1.getProduct()).thenReturn(product);
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(1);

        // Configuracion Item 2 (Producto distinto)
        when(item2.getProduct()).thenReturn(productB);
        when(item2.getPrice()).thenReturn(10.0);
        when(item2.getQuantity()).thenReturn(1);

        // Ejecucion
        order.addItem(item1);
        order.addItem(item2);

        // Verificaciones
        assertEquals(2, order.getItems().size(), "Se deberian tener dos elementos al ser productos distintos");
    }
}