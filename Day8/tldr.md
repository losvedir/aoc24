# Part 1

GPT-4o didn't work. I told it

> I ran it on the puzzle input and got
>
> That's not the right answer; your answer is too low.

> What are some plausible ways that the answer came out too low? Can you check
> the code to see if those issues are present and fix them?

It then explored some reasons and said it would write some code being more
precise, started to, and then just stopped. (Too long of context at that point?)

GPT-o1 worked out of the box.

Gemini runs but is wrong.

Claude Sonnet runs but it wrong. It identified (correctly) an issue with its
code when I told it it was wrong, but failed to fix it.

Claude Opus got it wrong, too.

# Part 2

Only attempted with o1, since that's the only one that got part 1.

It returned essentially correct code on the first time, but it failed to compile
because it used a `var` in a place that the java compiler was unable to infer
the type. When I gave it the error message, it gave me the explicit type to use.
I also had to change two instance of `int` to `Integer`.
