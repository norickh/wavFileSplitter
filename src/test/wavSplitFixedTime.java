package test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class WavSplitFixedTime {
	String fileName = "";
	AudioInputStream audioIn = null;
	AudioFormat format = null;
	long readBytes = 0;
	long frameSize;
	long frameLength;
	byte[] buf;
	SourceDataLine line = null;
	DataLine.Info info;
	String wavFileName;
	String wavFileNamePre;
	String wavSplitDir;
	
	public WavSplitFixedTime(File wavFile, float slotSec) {
		wavFileName = wavFile.getName();
		wavFileNamePre = getPreffix(wavFileName);
		if (!wavFile.exists() || wavFileName.endsWith("wav") == false) {
			System.err.println("File is not exist or is not wav format.");
			return;
		}
		
		//Read wav file to audio input stream and bytes array.
		readWav(wavFile);
		
		//Calcurate bytes of the slot from sec.
		float bytesPerSec = format.getFrameRate() * format.getFrameSize();
		int slotBytes = (int) (bytesPerSec * slotSec);
		
		//Calcurate how many time we repeat to write splitted wav files.
		int loopTime =  (int) (readBytes/slotBytes) + 1 ;
			System.out.println("Slot:"+slotSec+"sec, "+slotBytes+"bytes\tLoopTime:"+loopTime);
		
		//Make directory where splitted wav files are written to.
		wavSplitDir = getPreffix(wavFile.getPath()) +"-slot" +slotSec +"sec";
		File newfile = new File(wavSplitDir);
		newfile.mkdir();
		
		//Write split wav file
		for (int i = 0; i < loopTime; i++) {
			String index = String.format("%03d", (i+1));
			String wavSplitPath = wavSplitDir+ "/" + wavFileNamePre + "-split"+ index + ".wav";
			File wavSplit = new File(wavSplitPath);
			int startBytes = slotBytes*i;
			writeWavSplit(wavSplit, startBytes, slotBytes);
		}
	}
	
	public void readWav(File file) {
		try {

			FileInputStream fis = new FileInputStream(file);
			InputStream fileIn = new BufferedInputStream(fis);
			audioIn = AudioSystem.getAudioInputStream(fileIn);
			format = audioIn.getFormat();
			buf = new byte[audioIn.available()];
				System.out.println("AIS.available: "+ audioIn.available() + "\tByte Array Length: " + buf.length);
			audioIn.read(buf, 0, buf.length); //バイト配列にAudioInputStreamを格納
			frameSize = format.getFrameSize();
			frameLength = audioIn.getFrameLength();
			readBytes = frameSize * frameLength;
				System.out.println(
					"Frame Size: " + frameSize + "\tFrame Length: " + frameLength + "\tnBytesRead: " + readBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeWavSplit(File wavFile, int startBytes, int slotBytes) {
		try {
			InputStream byteIn = new BufferedInputStream( new ByteArrayInputStream(buf, startBytes, slotBytes) );
			AudioInputStream ais = new AudioInputStream(byteIn, format,  byteIn.available());
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
			System.out.println("Write arbital byte length of audio");
		}
		catch (Exception e) {	e.printStackTrace(); } 
	}
	
	/**
	 * ファイル名から拡張子を取り除いた名前を返します。
	 * @param fileName ファイル名
	 * @return ファイル名
	 */
	public static String getPreffix(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(0, point);
	    } 
	    return fileName;
	}
	
	public String getWavFileSplitDir(){
		return wavSplitDir;
	}

}
