import pandas as pd
import polars as pl

base_path = r"C:\Users\Red\Desktop\tese\Codigo_tese\Letra_Ricardo\FeatureExtractionSystem-master\merged_files"

def keep_top_features(csv_filename, top_features):
    # Read the CSV file into a DataFrame
    df = pl.read_csv(csv_filename,infer_schema_length=10000).to_pandas()

    # Keep only the top_features columns
    df_top_features = df.iloc[:, top_features]

    # then aloc just the first 4000 columns
    minimum_col = min(4000, len(df_top_features.columns))

    df_top_features = df_top_features.iloc[:, :minimum_col]

    # Create a new file name
    new_filename = csv_filename.rsplit('.', 1)[0] + '_top_%d_features_ranked.csv' % (minimum_col)

    df_top_features = pl.from_pandas(df_top_features)

    # now we want to append the "IdSong" column to the dataframe

    df_id_song = df.iloc[:, 0]

    df_id_song = pl.from_pandas(df_id_song)

    df_top_features = df_id_song.hstack(df_top_features)

    # Save the modified DataFrame to the new file
    df_top_features.write_csv(new_filename)
    print(f"File saved as {new_filename}")


keep_top_features(base_path + '/bimodal_full_features_without_CBF_good_features_with_lyric.csv', [210,16,7,21,6,251,327,23,13,4,214,5,328,250,18,3,215,324,206,12,329,249,212,207,308,29,258,326,213,281,253,25,254,292,1,229,24,26,218,231,11,269,216,270,252,217,259,303,130,28,17,266,230,221,232,22,300,241,27,10,321,239,299,168,316,323,307,34,138,311,66,56,35,238,245,271,60,244,169,247,15,318,325,30,139,267,233,165,237,275,282,260,309,75,36,132,242,175,220,235,234,225,208,305,42,291,40,63,176,289,228,58,209,276,179,137,280,223,264,257,310,277,227,302,68,91,222,140,211,224,55,41,178,114,117,93,265,20,278,50,8,64,43,125,330,263,74,9,279,95,84,57,83,240,154,204,313,71,76,304,80,90,122,274,89,312,112,51,147,298,149,32,184,256,185,14,79,109,164,293,192,31,167,290,288,246,319,273,201,160,59,199,103,123,272,306,88,39,115,134,205,113,243,202,47,54,166,19,301,155,314,189,85,200,295,33,128,153,143,219,119,163,188,136,106,182,99,156,148,101,65,320,97,255,286,102,194,44,297,283,317,62,193,49,118,170,226,195,236,173,157,203,53,69,86,46,72,100,124,87,135,78,92,98,296,161,177,108,96,82,37,105,73,186,187,174,322,172,45,107,315,268,294,129,248,144,127,120,52,133,38,61,142,150,162,196,285,159,287,261,191,104,67,131,190,152,151,81,181,121,126,94,171,180,2,77,198,116,146,183,284,262,141,110,70,48,111,197,158,145])


#print("[",end="")

#for i in range(0,3001):
    #print(i,end=",")

#print("]")
