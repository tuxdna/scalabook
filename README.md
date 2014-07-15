scalabook
==========

Book on Scala Programming Language

Install Asciidoctor:

    $ sudo gem install asciidoctor
    $ sudo gem install pygments.rb

Generate the book

    $ asciidoctor -a toc2 index.asciidoc

Open it in your favorite browser:

    $ firefox index.html


Use inotify tools for auto-generation:

    $ while inotifywait -e MODIFY *.asciidoc; do asciidoctor -a toc2 -a source-highlighter=pygments index.asciidoc ; done

