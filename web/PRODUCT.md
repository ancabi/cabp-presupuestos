# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Users

Una o dos personas de la propia empresa CABP (dueño / administración). Son ellas mismas quienes dan de alta clientes, preparan presupuestos, emiten facturas y guardan la documentación de cada pedido. Los clientes finales no entran en la aplicación: solo reciben los PDF.

Hay dos roles: administrador (gestiona usuarios, ajustes y tipos de fichero) y usuario.

Se usa tanto en el ordenador de la oficina como en el móvil o la tablet, a partes iguales; ninguno de los dos es secundario.

## Product Purpose

Herramienta interna de gestión comercial de CABP, empresa que vende, importa e instala salvaescaleras, plataformas y elevadores. Sustituye a una aplicación Java Swing de 2011 (conservada en `cabpTrunk/`).

Permite llevar en un solo sitio, para todos los años: clientes, distribuidores y sus productos, presupuestos, facturas, pedidos con su documentación (aduanas, facturas firmadas, fotos…) y adjuntos de cada cliente. El éxito es preparar un presupuesto o una factura correcta y presentable en pocos minutos, sin errores de numeración ni de importes, y encontrar después cualquier documento de un pedido.

## Positioning

No compite con un programa de facturación genérico: está hecha a la medida de cómo trabaja CABP. Recoge sus costes de instalación (hotel, pasaje, combustible, kilómetros, ganancia y recargo), la calculadora de escalera, sus textos de forma de pago y condiciones, y el ciclo presupuesto → factura → pedido con la documentación de importación.

## Operating Context

- Numeración por año: `P-2026/0001` (presupuestos) y `F-2026/0001` (facturas); se reinicia cada 1 de enero. En los PDF el número se muestra con guiones (`F-2026-0001`). Arriba a la derecha se elige el ejercicio que se está viendo.
- Los PDF se generan desde el navegador («Imprimir → Guardar como PDF») a partir de una vista A4. Son lo que ve el cliente final y llevan los datos de la empresa configurados en Ajustes (logo, NIF, dirección, cuenta bancaria). El aspecto del PDF (fuente, tamaño, colores, logo) se configura en Ajustes.
- En el bloque del cliente del PDF va primero la empresa del cliente (si la tiene) y debajo su nombre.
- Al convertir un presupuesto en factura se crea un pedido con nombre descriptivo. En el pedido se suben PDF, imágenes y texto (TXT), cada uno con un tipo («Documento aduana», «DUA aduana»…). La propia factura aparece siempre como primer fichero, de tipo «Factura cliente».
- Los datos históricos se importan desde los SQLite antiguos, uno por año.

## Capabilities and Constraints

- Backend Fastify + MariaDB; frontend React + Vite + Mantine; lógica común (cálculos, validaciones) en `shared/`, usada en pantalla y en servidor. Los importes se calculan en céntimos.
- Una factura solo se puede borrar si es la última de su año, para no dejar huecos. Un presupuesto no se puede convertir dos veces.
- Los tipos de fichero de pedidos se añaden y renombran, pero no se borran.
- Toda la interfaz está en español de España.
- Publicación con Docker detrás de un proxy inverso con HTTPS.

## Brand Commitments

- Nombre: «C A B P – Empresa de servicios». Logo en `/logo.png`, configurable en Ajustes.
- Los PDF de presupuestos y facturas son la cara de la empresa ante el cliente; su aspecto lo controla el usuario desde Ajustes → Aspecto del PDF.

## Evidence on Hand

- Datos reales de la empresa y textos de forma de pago y condiciones en `server/src/routes/ajustes.ts` (valores por defecto tomados de la plantilla Java).
- Aplicación original en `cabpTrunk/` como referencia de flujos.
- No hay testimonios, métricas de uso ni material de marketing: no inventarlos.

## Product Principles

1. **Lo correcto antes que lo rápido.** Numeración, IVA y totales nunca pueden quedar mal; la interfaz avisa antes de que se produzca un error.
2. **El PDF es el producto que ve el cliente.** Cualquier cambio en documentos se juzga por cómo queda impreso.
3. **Hecho a medida de un negocio pequeño.** Pocos usuarios que conocen bien su trabajo: flujos directos, sin pasos ni configuración que no necesiten.
4. **Igual de útil en el móvil que en la oficina.** Consultar un pedido o preparar un presupuesto debe funcionar bien en pantallas pequeñas.
5. **Nada se pierde.** Avisos al salir con cambios sin guardar, borrados con confirmación, documentación del pedido siempre localizable.
