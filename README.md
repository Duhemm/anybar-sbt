# AnyBar-sbt

This is a plugin that allows sbt to run new instances of AnyBar, and let's sbt communicate with it. For instance, AnyBar can be used to display the results of `test`.

Currently this is the only thing that the plugin does, but the ultimate goal is to make it easy to hook AnyBar into tasks, so that it can display the result of any sbt task.

## Installation

This is an AutoPlugin. Unfortunately, it is not yet published anywhere, which means that you will need to clone this repository, build it and publish the plugin locally. You can do it this way:

```
$ git clone https://github.com/Duhemm/anybar-sbt.git
$ cd anybar-sbt
$ sbt publishLocal
```

Then you're good to go and you can add it to your project definitions. Add this in your `project/plugins.sbt`:

```
addSbtPlugin("org.duhemm" % "sbt-anybar" % "0.1.0-SNAPSHOT")
```

You can now tell AnyBar to show you the results for the tasks you want:

```
lazy val myProject =
  (project in file(...)) settings (
    ...
    commands += showResultOf(test in Test),
    ...
  )
```

You can give any `TaskKey[_]` to `showResultOf`: Whenever you will run this task, the plugin will intercept the call and show the result in AnyBar.
