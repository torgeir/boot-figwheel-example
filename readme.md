# boot+figwheel example

A boot+figwheel example, that hot reloads cljs and less.

## prerequisits

```
brew install boot-clj
```

## run/watch

```
boot dev
```

## hot reload cljs and css

- visit `http://localhost:3000`
- change `core.cljs` to watch the js reload!
- change `app.main.less` to watch the css reload!

## connect the cljs browser repl

- connect to the repl server (e.g. with `cider-connect` in emacs, or [like this
in cursive](https://cursive-ide.com/userguide/repl.html#remote-repls). The port
number is logged when you run `boot dev`.)
- at the prompt `boot.user>` type `(boot-figwheel/cljs-repl)`
- type `(js/alert "woah!")` to watch an alert box pop open in the browser
- woah!
