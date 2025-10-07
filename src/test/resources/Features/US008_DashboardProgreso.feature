@US008 @dashboard @progreso @estudiante
Feature: Visualización de dashboard de progreso académico
  Como estudiante
  Quiero visualizar un dashboard con mis calificaciones y progreso en tiempo real
  Para saber si estoy avanzando de manera adecuada en mis cursos

  Scenario: Ver dashboard interactivo de progreso
    Given un estudiante con sesión activa
    When accede a la sección "Mi Progreso" desde el menú principal
    Then el sistema muestra dashboard interactivo con:
      | elemento               | descripción                         |
      | gráficos calificaciones| visualización por curso             |
      | porcentaje completitud | progreso en cada materia            |
      | comparativa promedio   | vs promedio del grupo               |
      | proyección final       | basada en rendimiento actual        |

  Scenario: Actualización tras nuevas calificaciones
    Given un profesor publica nuevas calificaciones
    When el estudiante recarga su dashboard de progreso
    Then el sistema actualiza automáticamente todas las métricas
    And muestra nuevas calificaciones obtenidas
    And recalcula promedios en tiempo real

  Scenario: Destacar cursos con bajo rendimiento
    Given un estudiante con calificaciones por debajo del promedio
    When visualiza su dashboard de progreso
    Then el sistema destaca materias con bajo rendimiento
    And usa indicadores de color rojo para alertar

