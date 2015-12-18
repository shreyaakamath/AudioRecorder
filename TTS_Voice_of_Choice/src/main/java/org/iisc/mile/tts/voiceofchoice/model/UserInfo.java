package org.iisc.mile.tts.voiceofchoice.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Entity
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int JUMPER_LENGTH = 401;

	@Id
	private String userId;

	private String name;

	private String dob;

	private String gender;

	private String parentGuardianName;

	private String address;

	private String contactNumber;

	private String emailId;

	private int lastRecordedKannadaSentence;

	private int lastRecordedTamilSentence;

	private int lastRecordedHindiSentence;

	private int lastRecordedEnglishSentence;

	@Lob
	private byte[] jumperArrayKannada;

	@Lob
	private byte[] jumperArrayTamil;

	@Lob
	private byte[] jumperArrayHindi;

	@Lob
	private byte[] jumperArrayEnglish;

	public UserInfo() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return this.dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getParentGuardianName() {
		return this.parentGuardianName;
	}

	public void setParentGuardianName(String parentGuardianName) {
		this.parentGuardianName = parentGuardianName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return this.contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getLastRecordedKannadaSentence() {
		return lastRecordedKannadaSentence;
	}

	public void setLastRecordedKannadaSentence(int lastRecordedKannadaSentence) {
		this.lastRecordedKannadaSentence = lastRecordedKannadaSentence;
	}

	public int getLastRecordedTamilSentence() {
		return lastRecordedTamilSentence;
	}

	public void setLastRecordedTamilSentence(int lastRecordedTamilSentence) {
		this.lastRecordedTamilSentence = lastRecordedTamilSentence;
	}

	public int getLastRecordedHindiSentence() {
		return lastRecordedHindiSentence;
	}

	public void setLastRecordedHindiSentence(int lastRecordedHindiSentence) {
		this.lastRecordedHindiSentence = lastRecordedHindiSentence;
	}

	public int getLastRecordedEnglishSentence() {
		return lastRecordedEnglishSentence;
	}

	public void setLastRecordedEnglishSentence(int lastRecordedEnglishSentence) {
		this.lastRecordedEnglishSentence = lastRecordedEnglishSentence;
	}

	public byte[] getJumperArrayKannada() {
		return this.jumperArrayKannada;
	}

	public void setJumperArrayKannada(byte[] jumperArrayKannada) {
		this.jumperArrayKannada = jumperArrayKannada;
	}

	public byte[] getJumperArrayTamil() {
		return this.jumperArrayTamil;
	}

	public void setJumperArrayTamil(byte[] jumperArrayKannadaTamil) {
		this.jumperArrayTamil = jumperArrayKannadaTamil;
	}

	public byte[] getJumperArrayHindi() {
		return this.jumperArrayHindi;
	}

	public void setJumperArrayHindi(byte[] jumperArrayHindi) {
		this.jumperArrayHindi = jumperArrayHindi;
	}

	public byte[] getJumperArrayEnglish() {
		return jumperArrayEnglish;
	}

	public void setJumperArrayEnglish(byte[] jumperArrayEnglish) {
		this.jumperArrayEnglish = jumperArrayEnglish;
	}

	public List<?> getUserIdList() {
		EntityManagerFactory entityManagerFactory = Persistence
				.createEntityManagerFactory(DatabaseUtil.JPA_NAME);
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		Query query = entityManager.createNativeQuery("SELECT UserId from UserInfo");
		List<?> list = query.getResultList();
		entityTransaction.commit();
		entityManager.close();
		entityManagerFactory.close();
		return list;
	}

	public int[] getJumperArrayInt(byte[] byteArray) {
		int jumperArray[] = new int[JUMPER_LENGTH];
		try {
			ByteArrayInputStream bin = new ByteArrayInputStream(byteArray);
			DataInputStream din = new DataInputStream(bin);
			for (int i = 0; i < JUMPER_LENGTH; i++) {
				jumperArray[i] = din.readInt();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return jumperArray;
	}

	public byte[] setJumperArrayInt(int[] jumperArray) {
		byte[] asBytes = new byte[JUMPER_LENGTH];
		try {

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream dout = new DataOutputStream(bout);
			for (int p = 0; p < JUMPER_LENGTH; p++) {
				dout.writeInt(jumperArray[p]);
			}
			dout.close();
			asBytes = bout.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return asBytes;
	}

}