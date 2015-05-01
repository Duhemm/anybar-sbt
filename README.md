# AnyBar-sbt

This is a plugin that allows sbt to run new instances of AnyBar, and let's sbt communicate with it. For instance, AnyBar can be used to display the results of `test`.

AnyBar-sbt can be used to hook AnyBar with any sbt task, and use AnyBar to display the results of running this task.

## Installation (Stable)

Add this in your `project/plugins.sbt`:

```
addSbtPlugin("com.github.duhemm" % "sbt-anybar" % "0.0.1")
```

You can now tell AnyBar to show you the results for the tasks you want:

```scala
lazy val myProject =
  (project in file(...)) settings (
    ...
    commands += showResultOf(test in Test),
    ...
  )
```

You can give any `TaskKey[_]` to `showResultOf`: Whenever you will run this task, the plugin will intercept the call and show the result in AnyBar.

## Installation (Snapshot)

Clone this repository and publish anybar-sbt locally:

```
$ git clone git@github.com:Duhemm/anybar-sbt.git anybar-sbt
$ cd anybar-sbt
$ sbt publishLocal
```

Add this to your plugins.sbt:

```scala
addSbtPlugin("com.github.duhemm" % "sbt-anybar" % "0.1.0-SNAPSHOT")
```

Add this to your build.sbt:

```scala
val instanceForTest = AnyBar.newInstance // Will start a new process listening on a random port
val instanceForCompilation = AnyBar newInstanceOnPort 1234 // Will start a new process listening on port 1234
val instanceForPublish = AnyBar alreadyRunningOnPort 1235 // Will communicate with existing instance on port 1235
...
lazy val myProject =
  project in file(...) settings (
    ...
    commands += instanceForTest showResultOf (test in Test),
    commands ++= instanceForCompilation showResultOf (compile in Compile, clean in Compile),
    commands += instanceForPublish showResultOf (publish in Compile),
    ...
  )
```
