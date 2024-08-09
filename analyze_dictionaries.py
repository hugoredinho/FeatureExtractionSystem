import matplotlib.pyplot as plt

base_path = r"C:\Users\Red\Desktop\tese\Codigo_tese\Letra_Ricardo\FeatureExtractionSystem-master\src\AuxiliarFiles"

def analyze_dictionary(file):
    with open(base_path + "\\" + file, "r") as f:
        lines = f.readlines()

    words = []
    for line in lines:
        line_split = line.strip()
        line_split = line_split.replace("  "," ")
        line_split = line_split.replace("\t"," ")
        line_split = line_split.replace("  "," ")
        line_split = line_split.split(" ")
        word = line_split[0]
        print(line_split)
        valence = float(line_split[-2])
        arousal = float(line_split[-1])
        words.append((word, valence, arousal))

    # Plotting
    plt.figure()
    plt.scatter([word[1] for word in words], [word[2] for word in words])
    plt.xlabel("Valence")
    plt.ylabel("Arousal")

    plt.show()

def count_words(dictionary, lyrics_file):
    with open(base_path + "\\" + dictionary, "r") as f:
        lines = f.readlines()

    words = []
    for line in lines:
        line_split = line.strip()
        line_split = line_split.replace("  "," ")
        line_split = line_split.replace("\t"," ")
        line_split = line_split.replace("  "," ")
        line_split = line_split.split(" ")
        word = line_split[0]
        words.append(word)

    with open(lyrics_file, "r") as f:
        lyrics = f.read()

    lyrics = lyrics.replace("\n", " ")
    # now we remove anything that's not a letter or within the allowed list
    allowed_list = ["'", " ","","-"]
    lyrics = "".join([char for char in lyrics if char.isalpha() or char in allowed_list])

    list_of_diff_chars = list(set(lyrics))
    print(list_of_diff_chars)

    # now we lower
    lyrics = lyrics.lower()
    lyrics = lyrics.split(" ")

    count = 0
    print(lyrics)

    for word in lyrics:
        if word in words:
            #print(word)
            count += 1
    
    print(count)

def read_dictionary(file):
    with open(base_path + "\\" + file, "r") as f:
        lines = f.readlines()

    words = []  

    for line in lines:
        line_split = line.strip()
        line_split = line_split.replace("  "," ")
        line_split = line_split.replace("\t"," ")
        line_split = line_split.replace("  "," ")
        line_split = line_split.split(" ")
        word = line_split[0]
        valence = float(line_split[-2])
        arousal = float(line_split[-1])
        words.append((word, valence, arousal))

    return words

def compare_dictionaries(first_dict, second_dict):
    # the idea is to get 10 words at random that are on both dictionaries, and then compare their values

    first_dict = read_dictionary(first_dict)
    second_dict = read_dictionary(second_dict)

    first_dict_words = [word[0] for word in first_dict]
    second_dict_words = [word[0] for word in second_dict]

    import random
    random_words = random.sample(first_dict_words, 20)
    words_in_both = []
    for word in random_words:
        if word in second_dict_words:
            words_in_both.append(word)

    # Function to scale values from 0 to 1
    def scale_to_0_1(value, old_min, old_max):
        return (value - old_min) / (old_max - old_min)
    
    max_arousal_first = max([word[2] for word in first_dict])
    min_arousal_first = min([word[2] for word in first_dict])

    max_valence_first = max([word[1] for word in first_dict])
    min_valence_first = min([word[1] for word in first_dict])

    for word in words_in_both:
        index_first = first_dict_words.index(word)
        index_second = second_dict_words.index(word)

        # Scale the arousal value for the first dictionary
        original_arousal = first_dict[index_first][2]
        original_valence = first_dict[index_first][1]
        scaled_arousal = scale_to_0_1(original_arousal, min_arousal_first, max_arousal_first)
        scaled_valence = scale_to_0_1(first_dict[index_first][1], min_valence_first, max_valence_first)

        print(f"Word: {word}")
        print(f"Warriner: {first_dict[index_first][0], scaled_valence, scaled_arousal}")
        print(f"NRC_VAD: {second_dict[index_second]}")

    

if __name__ == '__main__':
    #analyze_dictionary("NRC-VAD-Lexicon_VA_only.txt")
    #count_words("GazQ1Q2Q3Q4_dal.txt", r"C:\Users\Red\Desktop\tese\Codigo_tese\Letra_Ricardo\FeatureExtractionSystem-master\src\Origem\MT0013751364.txt")
    compare_dictionaries("Warriner.txt","NRC-VAD-Lexicon_VA_only.txt")