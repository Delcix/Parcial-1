# Examen Parcial 1 - CRUD Personal de Aseo (Java)

Prototipo Android nativo con Room (SQLite) para registrar actividades del personal de aseo universitario.

## Funcionalidades

- CRUD completo de registros.
- Campos: nombre, apellido, CIP, cargo, edificio, dia, hora 24h, descripcion, fotos (2-4), categoria, monto.
- Filtro de gastos por categoria (comida, viaticos, otros).
- Total de gastos con dos decimales.
- Exportacion a JSON y comparticion por correo con `Intent.ACTION_SEND`.

## Ejecutar en Android Studio

1. Abre Android Studio.
2. Selecciona **Open** y abre la carpeta del proyecto `ExamenParcial1`.
3. Espera el **Gradle Sync**.
4. Ejecuta en emulador o dispositivo con API 36 (Android 16).
5. Flujo recomendado de prueba:
   - Registrar actividad (con 2 a 4 fotos).
   - Ver registros.
   - Editar y eliminar.
   - Filtrar gastos por categoria.
   - Exportar/compartir JSON.

## Estructura principal

- `data/ActivityRecord.java` Entity de Room.
- `data/ActivityDao.java` consultas CRUD y filtros.
- `data/AppDatabase.java` base de datos SQLite con Room.
- `data/ActivityRepository.java` capa de acceso simplificada.
- `ui/FormActivity.java` formulario de crear/editar.
- `ui/ListActivity.java` listado, eliminar, exportar JSON.
- `ui/FilterActivity.java` filtro por categoria y total.
- `ui/adapter/ActivityAdapter.java` RecyclerView.
- `util/JsonExporter.java` generador de archivo JSON.
