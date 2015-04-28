# AnyBar-sbt

This is a plugin that allows sbt to run new instances of AnyBar, and let's sbt communicate with it. For instance, AnyBar can be used to display the results of `test`.

Currently this is the only thing that the plugin does, but the ultimate goal is to make it easy to hook AnyBar into tasks, so that it can display the result of any sbt task.

## Installation

Add this in your `project/plugins.sbt`:

```
addSbtPlugin("com.github.duhemm" % "sbt-anybar" % "0.0.1")
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
