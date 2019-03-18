Currencies example app
======================

Implementation notes
====================

- The app is based on the Model-View-Presenter architectural pattern, to which an additional "use cases" layer has
 been added, to decouple the core business logic from the presenters (inspired by the Clean Architecture).
- The UI part is single activity, and the two screens have been implemented as fragments. The navigation logic uses the new 
 Navigation library from the Android Architecture components, and it is described by a 
 navigation graph that can be found in the _res/navigation_ folder.
- For concurrency and background operations, Kotlin coroutines have been used.
- For REST api calls, Retrofit has been used. I have used a not-yet-released snapshot version of Retrofit, 
  because it has better integration with coroutines, allowing to declare retrofit methods directly as suspension functions.
  Although, in a real production project, I would go for the stable version instead. 
- Dependency injection: dagger 2 has been used, with dagger-android extensions
- Unit tests: JUnit, Mockito, and Mockito-Kotlin have been used
- UI tests: Espresso has been used

Known issues / possible improvements
====================================

- The back arrow in the second screen's action bar does not work, and I haven't had time to figure out why.
  I would expect the Android Navigation components to handle it automatically, but I'm probably missing some detail,
  being the first time I use this library. The back button, though, works as expected and can be used to navigate back.
- Formatting rules are missing when displaying numbers: a fixed amount of decimal digits and alignment 
  to the right would definitely look better.
- Unit tests have been written only for the _currencies/conversion_ package (first screen of the app), as a demonstration
of my ability to write them. In a real life scenario, I would have written them for all classes, except Android UI components 
(activities, fragments, views, adapters)
- Only a simple Espresso test is included, as an example. 
- Proguard rules are not in place, so please run the debug build only.. the release one is probably going to have some issues.

