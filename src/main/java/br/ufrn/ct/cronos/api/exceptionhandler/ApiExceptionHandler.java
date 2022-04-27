package br.ufrn.ct.cronos.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.ufrn.ct.cronos.core.validations.ValidacaoException;
import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.EntidadeNaoEncontradaException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;

//ResponseEntityExceptionHandler é uma classe base padrão que trata todas as exceptions internas do Spring MVC 
// para todos os métodos que tenha requisições mapeadas(métodos @RequestMapping)
@ControllerAdvice //centraliza o tratamento de excessões disparadas pelos Controllers.
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL
		= "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
				+ "o problema persistir, entre em contato com o administrador do sistema.";

	// MessageSource é uma interface q a implementação dela resolve mensagens
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler({ ValidacaoException.class })
	public ResponseEntity<Object> handleValidacaoException(ValidacaoException ex, WebRequest request) {
		return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), 
				HttpStatus.BAD_REQUEST, request);
	}
	
	/**
	 * Customiza a resposta para uma exceção HttpMessageNotReadableException.
	 * <p>Este tipo de Exceção é lançada quando a mensagem http não pode ser lida por erros de formatação, sintaxe, valores inadequados, etc.
	 * @param ex a exceção
	 * @param headers os cabeçalhos que serão repassados para um dos métodos: handleExceptionInternal, 
	 * 		handleInvalidFormatException e PropertyBindingException
	 * @param status o status de resposta que será repassado para para um dos métodos: handleExceptionInternal, 
	 * 		handleInvalidFormatException e PropertyBindingException
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// Classe ExceptionUtils da lib da lib commons-lang3 da Apache
		// método getRootCause retorna a causa raiz da exceção.. ele percorre toda a hierarquia/stack/pilha de 
		// exceções até chegar na causa/exceção raiz
		//Throwable rootCause = ExceptionUtils.getRootCause(ex);

		// Pegando a causa raiz (rootCause), eu não capturo adequadamente problema de formatação com data, pois
		// é retornado um DateTimeParseException, e não um InvalidFormatException.
		// Por isso preferi pegar a causa sem usar o ExceptionUtils como foi passado na Aula de Tiago (Algaworks)
		Throwable rootCause = ex.getCause();
		
		// Se for informada uma letra (sem aspas) para uma propriedade numérica, a exceção JsonParseException é lançada,
		// pois o JSON sequer é válido. Letras sempre tem q ser passada entre aspas

		// InvalidFormatException ocorre quando uma propriedade recebe um valor inadequado.
		// Ocorre quando não se consegue desserializar o valor, por ele estar inadequado (formatação incorreta)
		// Ex: Um inteiro q recebe letras/string. Uma data q recebe uma string qualquer inadequada para uma data.
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request); 
		}
		//System.out.println("##### ENTROU NO handleHttpMessageNotReadable e não é um InvalidFormatException, nem PropertyBindingException");
		// InvalidFormatException é uma exceção mais específica q HttpMessageNotReadableException.
		// HttpMessageNotReadableException diz respeito a requisição HTTP como um todo. Ex: Pode ser exceção de JSON mal escrito.
		// InvalidFormatException diz respeito apenas a serialização/desserialização da API
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique possível erro de sintaxe.";
		
		Problem problem = createProblemBuilder(status, problemType, detail)
						.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)		
						.build();
		
		// Na aula ficou passando uma nova instância de HttpHeaders, mas o ideal
		// é repassar o "headers" que recebemos como argumento do método
		// return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(
			EntidadeNaoEncontradaException ex, WebRequest request) {
				HttpStatus status = HttpStatus.NOT_FOUND;
				ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
				String detail = ex.getMessage();
				
				Problem problem = createProblemBuilder(status, problemType, detail)
								.userMessage(detail)
								.build();
				
				return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
            /*return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), 
				HttpStatus.NOT_FOUND, request);*/
	}
	
	/**
	 * Customiza a resposta para uma exceção NegocioException.
	 * <p>Este tipo de Exceção é lançada quando ocorre uma tentativa de violação de regra de negócio.
	 * @param ex a exceção
	 * @param headers os cabeçalhos que serão repassados para o método handleExceptionInternal
	 * @param status o status de resposta que será repassado para o método handleExceptionInternal
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();

		Problem problem = createProblemBuilder(status, problemType, detail)
						.userMessage(detail)
						.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
    
	/**
	 * Customiza a resposta para uma exceção EntidadeEmUsoException.
	 * <p>Este tipo de Exceção é lançada quando ocorre uma tentativa de exclusão de uma entidade em uso.
	 * @param ex a exceção
	 * @param headers os cabeçalhos que serão escritos na resposta
	 * @param status o status de resposta adequado
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
    @ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
    	ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
    	String detail = ex.getMessage();
    
    	Problem problem = createProblemBuilder(status, problemType, detail)
						.userMessage(detail)				
						.build();
    
    	return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
/*
	// 1. MethodArgumentTypeMismatchException é um subtipo de TypeMismatchException

	// 2. ResponseEntityExceptionHandler já trata TypeMismatchException de forma mais abrangente

	// 3. Então, especializamos o método handleTypeMismatch e verificamos se a exception
	//    é uma instância de MethodArgumentTypeMismatchException

	// 4. Se for, chamamos um método especialista em tratar esse tipo de exception

	// 5. Poderíamos fazer tudo dentro de handleTypeMismatch, mas preferi separar em outro método
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request) {
    
    	if (ex instanceof MethodArgumentTypeMismatchException) {
        	return handleMethodArgumentTypeMismatch(
            	    (MethodArgumentTypeMismatchException) ex, headers, status, request);
    	}

    	return super.handleTypeMismatch(ex, headers, status, request);
	}*/

	// Tratando exception de parâmetro de URL inválido
	/**
	 * Customiza a resposta para uma exceção MethodArgumentTypeMismatchException.
	 * <p>Este tipo de Exceção é lançada quando o parâmetro de uma URL é inválido.
	 * @param ex a exceção
	 * @param headers os cabeçalhos que serão escritos na resposta
	 * @param status o status de resposta adequado
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
    	return handleMethodArgumentTypeMismatch(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

    /*
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException() {
		ApiError problema = ApiError.builder()
				.dataHora(LocalDateTime.now())
				.mensagem("O tipo de mídia que você deseja enviar, não é aceito pela API.").build();
		
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
				.body(problema);
	}*/

	/**
	 * Customiza a resposta para uma exceção NoHandlerFoundException.
	 * <p>Este tipo de Exceção é lançada quando uma URL de recurso inválida/inexistente é informada.
	 * @param ex a exceção
	 * @param headers os cabeçalhos que serão repassados para o método handleExceptionInternal
	 * @param status o status de resposta que será repassado para o método handleExceptionInternal
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, 
        	HttpHeaders headers, HttpStatus status, WebRequest request) {
    	ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
    	String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", 
            	ex.getRequestURL());
    
    	Problem problem = createProblemBuilder(status, problemType, detail)
						.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
						.build();
    
    	return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/**
	 * Customiza a resposta para uma exceção não tratada que herda de java.lang.Exception.
	 * <p>Este método é executado quando uma exceção não capturada pelos outros métodos é lançada.
	 * @param ex a exceção
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
    	HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
    	ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

    	// Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
    	// fazendo logging) para mostrar a stacktrace no console
    	// Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
    	// para você durante, especialmente na fase de desenvolvimento
    	ex.printStackTrace();
    
    	Problem problem = createProblemBuilder(status, problemType, detail)
						.userMessage(detail)				
						.build();

    	return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}	

	/*@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        	HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("##### ENTROU NO handleMethodArgumentNotValid");
		ex.printStackTrace();
    	ProblemType problemType = ProblemType.DADOS_INVALIDOS;
    	String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
        
		// uma instância de BindingResult, armazena as violações de constraints de validação
		// Com o BindingResult, se tem acesso a quais fields/propriedades foram violadas
		BindingResult bindingResult = ex.getBindingResult();
		List<Problem.Field> problemFields = bindingResult.getFieldErrors().stream()
					.map(fieldError -> {
						// Locale representa uma região geográfica ou cultural
						String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
						return Problem.Field.builder()
								.name(fieldError.getField())
								.userMessage(message)
								.build();
					})
					.collect(Collectors.toList());

    	Problem problem = createProblemBuilder(status, problemType, detail)
        	.userMessage(detail)
			.fields(problemFields)
        	.build();
    
    	return handleExceptionInternal(ex, problem, headers, status, request);
	}*/

	/**
	 * Método por onde passam todas as exceções lançadas.
	 * <p>Este método repassa as informações para o método de mesmo nome da sua superclasse.
	 * @param ex a exceção
	 * @param headers os cabeçalhos que serão repassados para o método handleExceptionInternal
	 * @param status o status de resposta que será repassado para o método handleExceptionInternal
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
    @Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (body == null) {
			body = Problem.builder()
					.timestamp(OffsetDateTime.now())
					.title(status.getReasonPhrase())
					.status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.build();
		} else if (body instanceof String) {
			body = Problem.builder()
					.timestamp(OffsetDateTime.now())
					.title((String) body)
					.status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	/**
	 * Customiza a resposta para uma exceção InvalidFormatException.
	 * <p>Este tipo de Exceção é lançada quando não se consegue desserializar o valor, por ele estar inadequado.
	 * @param ex a exceção
	 * @param headers os cabeçalhos que serão escritos na resposta
	 * @param status o status de resposta adequado
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
				//String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
		String path = joinPath(ex.getPath());
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo '%s'.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
						.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)	
						.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/**
	 * Customiza a resposta para uma exceção PropertyBindingException.
	 * <p>Este tipo de Exceção é lançada quando não se consegue fazer o binding de alguma propriedade 
	 * 	(mapear o valor de uma propriedade JSON para um objeto Java).
	 * @param ex a exceção
	 * @param headers os cabeçalhos que serão escritos na resposta
	 * @param status o status de resposta adequado
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
    	// Criei o método joinPath para reaproveitar em todos os métodos que precisam
    	// concatenar os nomes das propriedades (separando por ".")
    	String path = joinPath(ex.getPath());
    
    	ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
    	String detail = String.format("A propriedade '%s' não existe. "
            	+ "Corrija ou remova essa propriedade e tente novamente.", path);

    	Problem problem = createProblemBuilder(status, problemType, detail)
						.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
						.build();
    
    	return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/**
	 * Customiza a resposta para uma exceção MethodArgumentTypeMismatchException.
	 * <p>Este tipo de Exceção é lançada quando o parâmetro de uma URL é inválido.
	 * @param ex a exceção
	 * @param headers os cabeçalhos que serão escritos na resposta
	 * @param status o status de resposta adequado
	 * @param request a requisição atual
	 * @return uma instância de ResponseEntity
	 */
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
        	MethodArgumentTypeMismatchException ex, HttpHeaders headers,
        	HttpStatus status, WebRequest request) {
		
    	ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

    	String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
            	+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
            	ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

    	Problem problem = createProblemBuilder(status, problemType, detail)
						.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
						.build();

    	return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
			ProblemType problemType, String detail) {
		
		return Problem.builder()
			.timestamp(OffsetDateTime.now())
			.status(status.value())
			.type(problemType.getUri())
			.title(problemType.getTitle())
			.detail(detail);
	}

	private String joinPath(List<Reference> references) {
    	return references.stream()
        	.map(ref -> ref.getFieldName())
        	.collect(Collectors.joining("."));
	}

	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
	    String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

	    List<Problem.Field> problemObjects = bindingResult.getAllErrors().stream()
	    		.map(objectError -> {
	    			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
	    			
	    			String name = objectError.getObjectName();
	    			
	    			if (objectError instanceof FieldError) {
	    				name = ((FieldError) objectError).getField();
	    			}
	    			
	    			return Problem.Field.builder()
	    				.name(name)
	    				.userMessage(message)
	    				.build();
	    		})
	    		.collect(Collectors.toList());
	    
	    Problem problem = createProblemBuilder(status, problemType, detail)
	        .userMessage(detail)
	        .validations(problemObjects)
	        .build();
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	}
    
}
