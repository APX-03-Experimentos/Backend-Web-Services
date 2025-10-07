@US020 @estados @entregas @estudiante
Feature: Estados de entrega
  Como estudiante
  Quiero ver el estado de cada entrega
  Para conocer en qué punto del proceso está mi trabajo

  Scenario: Visualizar estado actual de entrega
    Given un estudiante en la sección "Mis Entregas" del curso
    When selecciona el trabajo "Tarea de Álgebra"
    Then el sistema muestra estado actual con indicadores visuales
    And muestra fecha de última actualización

  Scenario: Actualización automática de estado
    Given un profesor califica una entrega estudiantil
    When asigna una calificación en el sistema
    Then el estado del trabajo se actualiza automáticamente
    And muestra inmediatamente el nuevo estado "Calificado" al estudiante

