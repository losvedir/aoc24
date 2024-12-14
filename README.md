# Advent of Code 2024 - LLM Shoot-out

Prompt:

> Please solve the following Advent of Code problem in idiomatic, modern Java.
> You can assume the input has been downloaded to input/day<n>.txt. Use the
> class name <Model>, in package day<n>part1.
>
> =====
>
> copy/paste from AoC
>
> =====

If it gets it right, then:

> That was correct! That unlocked Part 2. Please solve Part two now, in a new
> class <Model> in package day<n>part2. Feel free to copy/paste any relevant
> code from the first part.
>
> =====
>
> copy/paste from AoC
>
> =====

## Results

| Problem | GPT-4o                                | GPT-o1                                | GPT-o1 Pro                            | Gemini Pro 1.5                        | Gemini Flash 2.0                      | Sonnet                                | Opus                                  |
| ------- | ------------------------------------- | ------------------------------------- | ------------------------------------- | ------------------------------------- | ------------------------------------- | ------------------------------------- | ------------------------------------- |
| Day 1   | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: |
| 1p2     |                                       |                                       |                                       |                                       |                                       |                                       |                                       |
| 10p1    | y                                     | y                                     | y                                     | n                                     | y                                     | y                                     | n                                     |
| 10p2    | n                                     | y                                     | y                                     |                                       | y                                     | y                                     |                                       |
| 11p1    | n                                     | y                                     | y                                     | y                                     |                                       | y                                     | y                                     |
| 11p2    |                                       | y                                     | y                                     | y                                     |                                       | n                                     | n                                     |
| Day 12  |                                       |                                       | :white_check_mark: :x:                | :white_check_mark: :x:                | :x:                                   | :white_check_mark: :x:                | :x:                                   |
| Day 13  | :white_check_mark: :x:                | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :x:                                   | :x:                                   | :white_check_mark: :white_check_mark: | :white_check_mark: :x:                |

# Day 1

- Opus compilation failed because it failed to import `IntStream`, but that was
  a trivial fix (Click "Quick Fix" in VSCode), so I didn't count it against it

# Day 12

- None could get part 2. Only me! But the first attempt, I got the same wrong
  answer as GPT-o1 Pro. But the instructions called out

> Be especially careful when counting the fence around regions like the one full
> of type A plants; in particular, each section of fence has an in-side and an
> out-side, so the fence does not connect across the middle of the region (where
> the two B regions touch diagonally)

and eventually I figured it out.

# Day 13

Brute force works for Part 1, but Part 2 requires an algebraic solution. Only
GPT-o1+ and Sonnet identified the need, and were able to solve it that way.

With prompting Opus made noises like it understood the algebraic solution, but
it didn't work.
