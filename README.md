scalabook
==========

Book on Scala Programming Language

To generate the book:

    $ sudo gem install asciidoctor

Create the presentation:

    $ asciidoctor -a toc2 index.asciidoc

Open it in your favorite browser:

    $ firefox index.html


Use inotify tools for auto-generation:

    $ while inotifywait -e MODIFY *.asciidoc; do asciidoctor -a toc2 -a source-highlighter=pygments index.asciidoc ; done

