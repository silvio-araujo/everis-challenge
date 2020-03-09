package br.com.silvio.everis.contacts.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.silvio.everis.contacts.dao.ContactDao;
import br.com.silvio.everis.contacts.exceptions.*;
import br.com.silvio.everis.contacts.model.Contact;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class ContactServiceImplTest {
	private static final Logger logger = LoggerFactory.getLogger(ContactServiceImplTest.class);
	private static final String failNoData = "Não foi possível fazer o teste sem nenhum contato cadastrado!";
	
	@Autowired
	private ContactService service;
	
	@Autowired
	private ContactDao contactDao;
	
	private static List<Contact> contacts;

	private void logAndFail(String msg) {
		logger.error(msg);
		fail(msg);
	}
	
	@Test
	@Order(1)
	public void testLoadContacts() {
		logger.info("*** Teste de carga de todos os contatos ***");
		
		contacts = service.loadContacts();
		Long qty = contactDao.count();
		
		logger.info("Foram carregados {} contatos, e contabilizados {} contatos no banco.", contacts.size(), qty);
		
		assertEquals(contacts.size(), contactDao.count());
	}
	
	@Test
	@Order(2)
	public void testLoadContactAddresses() {
		logger.info("*** Teste de carga de todos os endereços de um contato ***");

		if (contacts.size() > 0) {
			var firstContactId = contacts.get(0).getId();
			var executable = new Executable() {
				private Long id;
				
				@Override
				public void execute() throws ResourceNotFound {
					var addresses = service.loadContactAddresses(id);
					logger.info("Foram carregados {} endereços pertencentes ao contato de ID {}", addresses.size(), id);
				}
				
				public void setId(Long id) {
					this.id = id;
				}
			};
			
			try {
				executable.setId(firstContactId);
				executable.execute();
			} catch (RuntimeException e) {
				logger.error("Falha ao carregar os endereços do ID {}", firstContactId);
				logAndFail(e.getMessage());
			}
			
			executable.setId(0L);
			Assertions.assertThrows(ResourceNotFound.class, executable);			
		} else {
			logAndFail(failNoData);
		}
	}

	@Test
	@Order(3)
	public void testLoadContactPhones() {
		logger.info("*** Teste de carga de todos os telefones de um contato ***");

		if (contacts.size() > 0) {
			var firstContactId = contacts.get(0).getId();
			var executable = new Executable() {
				private Long id;
				
				@Override
				public void execute() throws ResourceNotFound {
					var phones = service.loadContactPhones(id);
					logger.info("Foram carregados {} telefones pertencentes ao contato de ID {}", phones.size(), id);
				}
				
				public void setId(Long id) {
					this.id = id;
				}
			};
			
			try {
				executable.setId(firstContactId);
				executable.execute();
			} catch (RuntimeException e) {
				logger.error("Falha ao carregar os telefones do ID {}", firstContactId);
				logAndFail(e.getMessage());
			}
			
			executable.setId(0L);
			Assertions.assertThrows(ResourceNotFound.class, executable);			
		} else {
			logAndFail(failNoData);
		}
	}

	@Test
	@Order(4)
	public void testLoadContactById() {
		logger.info("*** Teste de carga de um contato pelo ID ***");

		if (contacts.size() > 0) {
			var firstContactId = contacts.get(0).getId();
			var executable = new Executable() {
				private Long id;
				private Contact contact;
				
				@Override
				public void execute() throws Invalid {
					contact = service.loadContactById(id);
					if (contact == null) {
						logger.info("Contato de ID {} inexistente", id);
					} else {
						logger.info("Contato de ID {} carregado: {}", id, contact);
					}
				}
				
				public void setId(Long id) {
					this.id = id;
				}
				
				public Contact getContact() {
					return contact;
				}
			};
			
			try {
				executable.setId(firstContactId);
				executable.execute();
			} catch (RuntimeException e) {
				logger.error("Falha ao carregar o contato de ID {}", firstContactId);
				logAndFail(e.getMessage());
			}
			Assertions.assertNotNull(executable.getContact());
			
			try {
				executable.setId(0L);
				executable.execute();
			} catch (RuntimeException e) {
				logger.error("Falha ao carregar o contato de ID {}", firstContactId);
				logAndFail(e.getMessage());
			}
			Assertions.assertNull(executable.getContact());

			executable.setId(null);
			Assertions.assertThrows(Invalid.class, executable);			
		} else {
			fail(failNoData);
		}
	}

	@Test
	public void testAddContact() {
		logger.info("*** Teste de carga de adição de contatos ***");

		var executable = new Executable() {
			private Contact contactIn;
			private Contact contactOut;
			
			@Override
			public void execute() {
				this.contactOut = service.addContact(contactIn);
			}
			
			public void setContact(Contact contact) {
				this.contactIn = contact;
			}
			
			public Contact getContact() {
				return contactOut;
			}
		};
		
		try {
			executable.setContact(null);
			Assertions.assertThrows(RecordNotSupplied.class, executable, "Aceitando inclusão sem registro fornecido");
			
			Contact contact = new Contact();
			executable.setContact(contact);
			
			Assertions.assertThrows(Mandatory.class, executable, "Aceitando inclusão com nome nulo");
			
			contact.setId(5L);
			Assertions.assertThrows(IdSuppliedForNew.class, executable, "Aceitando inclusão com ID definido no registro");
			
			contact.setId(null);
			contact.setName("ABCDEF");
			Assertions.assertThrows(Invalid.class, executable, "Aceitando inclusão com nome curto");
			
			contact.setName("ABCDEFGHIJ");
			executable.execute();
			Contact writtenContact = executable.getContact();
			
			Assertions.assertNotNull(writtenContact, "Inclusão de registro correto foi recusada");
			Assertions.assertNotNull(writtenContact.getId(), "Inclusão de registro correto não retornou o ID no novo registro");
			
			contact.setId(null);
			contact.setCpf("123");
			Assertions.assertThrows(Invalid.class, executable, "Inclusão de registro com CPF curto foi aceita");
			
			contact.setCpf("1234567890A");
			Assertions.assertThrows(Invalid.class, executable, "Inclusão de registro com CPF alfanumérico foi aceita");
			
			contact.setCpf("12345678901");
			executable.execute();
		} catch (RuntimeException e) {
			logAndFail(e.getMessage());
		}
	}
}
