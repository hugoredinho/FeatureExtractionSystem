import os
import csv
import polars as pl
import pandas as pd
import tqdm
import shutil

'''
def check_and_remove_columns(df, file_name, columns_to_remove):
    removed_columns = []
    columns_lower = [col.lower() for col in df.columns]
    for col in columns_to_remove:
        if col in columns_lower:
            col_capitalized = col[0].upper() + col[1:]
            df.drop(col_capitalized, axis=1, inplace=True)
            removed_columns.append(col)
    if removed_columns:
        print(f"Removed columns from {file_name}: {', '.join(removed_columns)}")
    return df
def merge_csvs_and_remove_columns(folder_path):
    # Columns to remove (in various cases)
    columns_to_remove = ["quadrant", "quadrante", "classe"]

    first_file = True
    merged_df = None

    for file_name in sorted(os.listdir(folder_path)):
        if file_name.endswith('.csv'):
            print("Processing file:", file_name)
            file_path = os.path.join(folder_path, file_name)
            
            df = pd.read_csv(file_path)
            
            if not first_file:
                # If it's not the first file, drop the 'Id' column
                df = df.drop('Id', axis=1)
            else:
                first_file = False

            # Check and remove specified columns
            df = check_and_remove_columns(df, file_name, columns_to_remove)

            # Concatenate incrementally
            if merged_df is None:
                merged_df = df
            else:
                print("Concating file %s" % file_name)
                merged_df = pd.concat([merged_df, df], axis=1)

    print("Outputting to csv")
    file_name = "lyric_full_all_features.csv"
    merged_df.to_csv(file_name, index=False)

    number_of_columns = len(merged_df.columns)
    print(f"Number of columns: {number_of_columns}")
    print(f"Saved merged dataframe to {file_name}.")'''

def check_and_remove_columns(df, file_name, columns_to_remove):
    removed_columns = []
    columns_lower = [col.lower() for col in df.columns]

    for col in columns_to_remove:
        if col in columns_lower:
            col_capitalized = col[0].upper() + col[1:]
            df = df.drop(col_capitalized)
            removed_columns.append(col)
    if removed_columns:
        print(f"Removed columns from {file_name}: {', '.join(removed_columns)}")
    return df

def merge_csvs(folder_path, file_name_final):
    dfs = []  # List to hold DataFrames

    first_file = True
    total_columns = 0

    for file_name in sorted(os.listdir(folder_path)):
        if file_name.endswith('.csv'):
            print(f"Processing file: {file_name}")
            file_path = os.path.join(folder_path, file_name)
            df = pl.read_csv(file_path)
            

            if not first_file:
                df = df.drop("Id")
            else:
                first_file = False

            columns = df.columns

            total_columns += len(columns)

            dfs.append(df)

    # Concatenate all DataFrames horizontally
    merged_df = pl.concat(dfs, how = "horizontal")

    # Rename 'Id' column to 'IdSong'
    merged_df = merged_df.rename({"Id":"IdSong"})

    # Replace missing values with 0
    merged_df = merged_df.fill_nan(value=0)

    print(f"Outputting to csv: {file_name_final}")
    merged_df.write_csv(file_name_final)

    assert (total_columns == merged_df.width)

    print(f"Data shape: {merged_df.shape}")
    print(f"Saved merged dataframe to {file_name_final}.")

def make_annotations_file(file_name,folder_path):
    #previous_file = r"C:\Users\Red\Desktop\Investigacao2020\datasets\dataset_180\180_info_new.csv"~
    previous_file = r"C:\Users\Red\Desktop\tese\Final 230810 Datasets\complete lyrical dataset\merge_lyrics_complete_metadata.csv"
    dataframe_features = pl.read_csv(file_name + "_features.csv",columns=["IdSong"])

    # this is a series so we need to covert to list
    order = dataframe_features["IdSong"].to_list()

    dictionary = {}

    columns = ["Song","Quadrant"]

    df = pd.read_csv(previous_file, header = 0)

    new_dataframe = pd.DataFrame(columns=columns)

    for index, row in df.iterrows():
        # need to use concat here
        #print(row)
        dictionary[row["Song"].upper()] = row["Quadrant"]
        
  
    for value in order:
        value_upper = value.upper()
        if value_upper in dictionary:
            # need to use concat
            #new_dataframe = new_dataframe.append({"Song":value_upper,"Quadrant":dictionary[value_upper]},ignore_index=True)
            new_dataframe = pd.concat([new_dataframe,pd.DataFrame([[value,dictionary[value_upper]]],columns=columns)],ignore_index=True)

    print(new_dataframe)

    print(len(new_dataframe), len(order))
    assert(len(new_dataframe) == len(order))

    new_dataframe.to_csv("%s_annotations.csv" % (file_name), index=False)



def check_csv_for_id_column(folder_path):
    files_without_id = []
    
    # Iterate through each file in the folder
    for file_name in os.listdir(folder_path):
        if file_name.endswith('.csv'):
            file_path = os.path.join(folder_path, file_name)
            
            with open(file_path, 'r') as csv_file:
                reader = csv.reader(csv_file)
                
                # Get the header row
                header = next(reader, None)
                
                if header and "Id" not in header:
                    files_without_id.append(file_name)
                    
    if files_without_id:
        print("Files without 'Id' column:")
        for file_name in files_without_id:
            print(file_name)
    else:
        print("All CSV files have the 'Id' column.")

def process_csv_files(folder_path, file_name_final):
    dfs = []  # List to hold DataFrames

    first_file = True
    total_columns = 0
    total_files = os.listdir(folder_path)
    # List all files in the directory
    for filename in os.listdir(folder_path):
        # Check if it's a csv file
        if filename.endswith('.csv'):
            print("Processing " + filename)
            file_path = os.path.join(folder_path, filename)

            current_index = os.listdir(folder_path).index(filename)

            if "CBF_trig_st" in filename:
                continue
            
            # Read the csv file into a polars DataFrame
            df = pl.read_csv(file_path)
            df = df.fill_nan(0)

            columns = df.columns

            total_columns += len(columns)


            # Check if the first column's name is not 'Id' and rename it
            if df.columns[0] != 'Id':
                first_column_name = df.columns[0]
                df = df.rename({first_column_name: 'Id'})
                
            
            # Remove "/" and "_with_POStags" from the 'Id' column
            df = df.with_columns(df['Id'].str.replace("/", "").alias("Id"))
            df = df.with_columns(df['Id'].str.replace("_with_POStags", "").alias("Id"))
            df = df.with_columns(df['Id'].str.replace(".txt", "").alias("Id"))

            if not first_file:
                df = df.drop("Id")
            else:
                first_file = False

            # then we drop the columns called "classe", "quadrant" or "quadrant"

            if "Classe" in df.columns:
                df = df.drop("Classe")

            if "Quadrant" in df.columns:
                df = df.drop("Quadrant")

            if "Quadrante" in df.columns:
                df = df.drop("Quadrante")

            
            csv_file_without_csv = filename[:-4]

            # Create new column names (unless the column is 'Id')
            new_columns = {}
            for col in tqdm.tqdm(df.columns, desc="Renaming columns"):
                if col != 'Id' and f"{csv_file_without_csv}_" not in col:
                    new_columns[col] = f'{csv_file_without_csv}_{col}'


            # Rename the columns
            df = df.rename(new_columns)

            # Save the modified DataFrame back to the csv file
            df.write_csv(file_path)
            print("Processed %s, %d/%d, current data shape: %s" % (filename, len(dfs), len(total_files), df.shape))

            dfs.append(df)
            if filename == "":
                break

    
    # Concatenate all DataFrames horizontally
    merged_df = pl.concat(dfs, how = "horizontal")

    # Rename 'Id' column to 'IdSong'
    merged_df = merged_df.rename({"Id":"IdSong"})

    # Replace missing values with 0
    merged_df = merged_df.fill_nan(value=0)

    print(f"Outputting to csv: {file_name_final}")
    merged_df.write_csv(file_name_final)


    print(f"Data shape: {merged_df.shape}")

    print(merged_df.head())
    print(f"Saved merged dataframe to {file_name_final}. with shape {merged_df.shape}.")
    print(total_columns, merged_df.width)
    print("The final file has %d rows and %d columns" % (merged_df.height, merged_df.width))
    #assert (total_columns == merged_df.width)


def read_csvs_and_print_rows_number(folder_path):
    # Iterate through each file in the folder
    for file_name in os.listdir(folder_path):
        if file_name.endswith('.csv'):
            file_path = os.path.join(folder_path, file_name)
            
            df = pl.read_csv(file_path, has_header=True, columns=[0])

            print(file_name, len(df))

def add_lyrics_to_names(csv_file,audio=False):
    # Get total number of rows in the CSV for tqdm progress bar
    with open(csv_file, 'r', encoding='utf-8') as f:
        total_rows = sum(1 for row in f)

    if audio:
        prefix = "audio"
        big_prefix = "Audio"
    else:
        prefix = "lyric"
        big_prefix = "Lyric"



    output_filename = csv_file[:-4] + "_with_%s.csv" % (prefix)

    print("Output filename:", output_filename)

    # Now, process the CSV file
    with open(csv_file, 'r', newline='', encoding='utf-8') as infile, \
        open(output_filename, 'w', newline='', encoding='utf-8') as outfile:

        reader = csv.reader(infile)
        writer = csv.writer(outfile)

        # Handle header
        header = next(reader)
        new_header = [big_prefix + "_" + col if (col != 'Id' and col != "IdSong") else col for col in header]
        writer.writerow(new_header)

        # Copy the rest of the rows unchanged with tqdm progress bar
        for row in tqdm.tqdm(reader, total=total_rows-1, desc="Processing rows"):  # -1 for the header row
            writer.writerow(row)
  

def replace_mt_with_lyric_codes(file_name):
    #original_file_name = r"C:\Users\Red\Desktop\tese\Final 230810 Datasets\balanced_bimodal\merge_bi_modal_balanced_metadata.csv"
    #original_file_name = r"C:\Users\Red\Desktop\tese\Final 230810 Datasets\complete bimodal dataset\merge_bi_modal_complete_metadata.csv"
    original_file_name = r"C:\Users\Red\Desktop\tese\Final 230810 Datasets\complete lyrical dataset\merge_lyrics_complete_metadata.csv"
    #lyric_file = "bimodal_full_features_good_features_with_lyric.csv"
    lyric_file = "lyric_full_features.csv"
    #lyric_file = "bimodal_balanced_features_good_features_with_lyric.csv"

    df_to_change = pd.read_csv(file_name)

    df_original = pd.read_csv(original_file_name)

    df_lyric = pd.read_csv(lyric_file,usecols=[0])

    df_lyric_id_sorted = df_lyric["IdSong"].tolist()

    dictionary = {}

    for index, row in df_original.iterrows():
        row_audio_song = row["Song"]
        dictionary[row_audio_song] = row["Song"]
        dictionary[row_audio_song.upper()] = row["Song"]
        dictionary[row_audio_song.lower()] = row["Song"]


    for index, row in df_to_change.iterrows():
        # first we check if even need to change
        if row["Id"] in dictionary:
            df_to_change.at[index, "Id"] = dictionary[row["Id"]]
            print("Changed %s to %s" % (row["Id"], dictionary[row["Id"]]))
        else:
            print("Didn't change %s" % (row["Id"]))
            exit(-1)


    print("Lenght of lyric: %d" % len(df_lyric_id_sorted))
    print("Lenght of audio: %d" % len(df_to_change))

    # now we want to change the order so that it matches the lyric file
    new_df = pd.DataFrame(columns=df_to_change.columns)

    for value in tqdm.tqdm(df_lyric_id_sorted):
        #print(value)
        if (df_to_change.loc[df_to_change["Id"] == value].empty):
            value_lower = value.lower()
            new_df = pd.concat([new_df,df_to_change.loc[df_to_change["Id"] == value_lower]],ignore_index=True)

            if (df_to_change.loc[df_to_change["Id"] == value_lower].empty):
                value_upper = value.upper()
                new_df = pd.concat([new_df,df_to_change.loc[df_to_change["Id"] == value_upper]],ignore_index=True)

                if (df_to_change.loc[df_to_change["Id"] == value_upper].empty):
                    print("Couldn't find %s" % value)
                    exit(-1)
                else:
                    continue
            else:
                continue

        new_df = pd.concat([new_df,df_to_change.loc[df_to_change["Id"] == value]],ignore_index=True)

    # now we rename the Id column to IdSong
    new_df = new_df.rename(columns={"Id":"IdSong"})

    print(new_df.shape)
    print(new_df.head())

    new_df.to_csv(file_name.replace(".csv","_fix.csv"), index=False)


    #df_to_change.to_csv(file_name.replace(".csv","_fix.csv"), index=False)

def read_both_and_print():
    audio_file = "merged_squared_balanced_bimodal_not_sorted_with_audio_fix.csv"
    lyric_file = "bimodal_balanced_features_good_features_with_lyric.csv"

    df_audio = pd.read_csv(audio_file,usecols=[0])
    df_lyric = pd.read_csv(lyric_file,usecols=[0])

    df_audio_id_sorted = df_audio["IdSong"].sort_values().tolist()
    df_lyric_id_sorted = df_lyric["IdSong"].sort_values().tolist()

    for i in range(len(df_audio_id_sorted)):
        if df_audio_id_sorted[i] != df_lyric_id_sorted[i]:
            print("Not equal: %s and %s" % (df_audio_id_sorted[i], df_lyric_id_sorted[i]))

def go_through_folder_and_print_csv_rows(folder_path):
    for file_name in os.listdir(folder_path):
        if file_name.endswith('.csv'):
            file_path = os.path.join(folder_path, file_name)
            
            #df = pl.read_csv(file_path, infer_schema_length=100000)
            df = pl.read_csv(file_path)

            print(file_name, len(df), df.width)
    
def merge_audio_and_lyric_files():
    #audio_file = "full_bimodal_final_all_features_with_audio_fix.csv"
    #lyrics_file = "bimodal_full_features_good_features_with_lyric.csv"
    #lyrics_file = "bimodal_balanced_features_good_features_with_lyric.csv"
    #audio_file = "balanced_bimodal_final_all_features_with_audio_fix.csv"
    audio_file = "133_audio_features.csv"
    lyrics_file = "133_features_good_features_with_lyric.csv"

    df_lyrics = pd.read_csv(lyrics_file)
    df_audio = pd.read_csv(audio_file)

    # first we assure that the df_lyrics["IdSong"] and df_audio["IdSong"] are the same

    df_lyrics_id = df_lyrics["IdSong"].tolist()

    # now we conver df_lyrics_id to list but can't use pandas tolist() because it returns a series

    df_audio_id = df_audio["IdSong"].tolist()

    for i in range(len(df_audio_id)):
        print(df_audio_id[i], df_lyrics_id[i])

    assert(df_lyrics_id == df_audio_id)

    # now we just merge the two files, dropping the IdSong from the audio dataframe

    df_audio = df_audio.drop("IdSong", axis=1)

    df_merged = pd.concat([df_lyrics,df_audio],axis=1)

    print("Audio has %d rows and %d columns" % (len(df_audio), df_audio.shape[1]))
    print("Lyrics has %d rows and %d columns" % (len(df_lyrics), df_lyrics.shape[1]))

    print("Merged has %d rows and %d columns" % (len(df_merged), df_merged.shape[1]))

    df_merged.to_csv("133_audio_lyrics.csv", index=False)


def change_columns():
    #lyric_file = "bimodal_balanced_features_good_features_with_lyric.csv"
    #audio_file = "merged_squared_balanced_bimodal_not_sorted_with_audio_fix.csv"
    audio_file = "full_bimodal_final_all_features_with_audio_fix.csv"
    lyric_file = "bimodal_full_features_good_features_with_lyric.csv"

    df_lyric = pd.read_csv(lyric_file,usecols=["IdSong"])
    df_audio = pd.read_csv(audio_file)

    for index, row in tqdm.tqdm(df_lyric.iterrows()):
        lyric_idsong = row["IdSong"].upper()
        # now we want to get the audio value at the same index
        audio_row = df_audio.at[index, "IdSong"].upper()

        if lyric_idsong != audio_row:
            print("Not equal: %s and %s" % (lyric_idsong, audio_row))
            exit(-1)

        # now we want to chnage the audio_row value to the lyric_idsong
        df_audio.at[index, "IdSong"] = row["IdSong"]
        print("Changing %s to %s" % (audio_row, row["IdSong"]))

    df_audio.to_csv(audio_file, index=False)

def merge_folders():

    # need to make code here to get rid of "classe" etc

    folder_path = r"src/Feature Stuff/133_features"
    #check_csv_for_id_column("src/Output")
    print("--------------PROCESSING CSV FILES---------------\n\n\n")
    #process_csv_files(folder_path,"133_features.csv")
    print("--------------MAKIG ANNOTATIONS---------------\n\n\n")
    make_annotations_file("133",folder_path)
    #go_through_folder_and_print_csv_rows(folder_path)

def make_180_audio_features():
    lyric_180_file = "133_audio_features.csv"
    audio_full_file = "lyric_full_features.csv"

    df_lyric = pd.read_csv(lyric_180_file,usecols=["IdSong"])

    df_audio = pd.read_csv(audio_full_file)

    # now we want to keep the rows from df_audio where IdSong is in df_lyric

    df_audio = df_audio[df_audio["IdSong"].isin(df_lyric["IdSong"])]

    print(df_audio.shape)

    df_audio.to_csv("133_lyric_features.csv", index=False)

def copy_133():
    file = "133_audio_features.csv"

    df = pd.read_csv(file)

    original_path = r"C:\Users\Red\Desktop\tese\Final 230810 Datasets\complete lyrical dataset\all"

    new_path = r"133_lyrics"

    for index, row in tqdm.tqdm(df.iterrows()):
        row_song_upper = row["IdSong"].upper()

        file_path = os.path.join(original_path, row_song_upper + ".txt")

        if os.path.exists(file_path):
            shutil.copy(file_path, new_path)

        else:
            print("Couldn't find %s" % file_path)
            exit(-1)

if __name__ == "__main__":
    #file_path = "src/.csv" 
    #file_path = r"C:\Users\Red\Desktop\tese\Codigo_tese\Letra_Ricardo\FeatureExtractionSystem-master\src\Feature Stuff\teste\teste.csv."
    #check(file_path)
    #merge_folders()
    #go_through_folder_and_print_csv_rows("src/Feature Stuff/bimodal_full_features")

   #
    #add_lyrics_to_names("full_audio_all_features.csv",audio=True)
    #add_lyrics_to_names("133_features_good_features.csv",audio=False)
    #replace_mt_with_lyric_codes("full_audio_all_features_with_audio.csv")
    merge_audio_and_lyric_files()
    #make_180_audio_features()
    #copy_133()
    #change_columns()

    #df = pl.read_csv(file_path)

    #rows_length = len(df)

    #print(rows_length)
    #print(df.shape)

    #df.write_csv("bimodal_full_features_test.csv")
    #read_both_and_print()

    #print(df.shape)


